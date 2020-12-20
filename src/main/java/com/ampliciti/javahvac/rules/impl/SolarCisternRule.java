/*
 * Copyright (C) 2019-2020 jeffrey
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.dao.domain.SourceOverride;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.MiscNotices;
import com.ampliciti.javahvac.domain.NodeInformation;
import com.ampliciti.javahvac.domain.NodeSourceInformation;
import com.ampliciti.javahvac.domain.config.Node;
import com.ampliciti.javahvac.exceptions.NodeConnectionException;
import com.ampliciti.javahvac.rules.Rule;
import com.ampliciti.javahvac.service.DaylightService;
import com.ampliciti.javahvac.service.NodeService;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Rule for maintaining a solar cistern.
 *
 * @author jeffrey
 */
public class SolarCisternRule implements Rule {

  /**
   * Maximum temp that we'll allow our cistern to get to.
   */
  private float maxTemp;

  /**
   * Name for this solar cistern.
   */
  private String name;

  /**
   * Node Service for us to use.
   */
  private NodeService nodeService;

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(SolarCisternRule.class);

  /**
   * Temp at cistern inlet from panels.
   */
  private Double cisternInletTemp = null;
  /**
   * Temp at cistern top.
   */
  private Double cisternTopTemp = null;
  /**
   * Temp at cistern bottom.
   */
  private Double cisternBottomTemp = null;

  /**
   * Average temp between both sensors in the cistern.
   */
  private Double cisternAverageTemp = null;

  /**
   * Current temp gain from panels to cistern.
   */
  private Double tempGain = null;

  /**
   * Last time in ms that the recirculator ran.
   */
  private long lastRecirculatorRunTime = 0l;

  /**
   * Nodes that control/read from the cistern.
   */
  private ArrayList<Node> cisternNodes = null;

  /**
   * Current state of the recirculator.
   */
  private boolean recirculatorState = false;

  /**
   * Max time we'll let the cistern run while losing heat.
   */
  private long maxColdRuntime = 1000 * 60 * 5;// 5 min

  /**
   * How long we'll wait before checking to see if things are better.
   */
  private long retryTime = 1000 * 60 * 15;// 15 minutes

  /**
   * Constructor.
   *
   * @param name Name for this solar cistern.
   * @param maxTemp Maximum temp that we'll allow our cistern to get to.
   */
  public SolarCisternRule(String name, float maxTemp) {
    this.maxTemp = maxTemp;
    this.name = name;
    this.nodeService = new NodeService();

  }

  /**
   * Friendly string that defines what this rule is.
   *
   * @return
   */
  @Override
  public String getDefinition() {
    return "Solar cistern rule for: " + name;
  }

  /**
   * A call to this method indicates that this rule should be actively enforced in the real world.
   *
   * @return True if the rule was successfully implemented, false if the condition could not be
   *         enforced.
   */
  @Override
  public synchronized boolean enforceRule() {
    // NOTE: This is rather specific to my setup. Either override with your own rules, or use the
    // exact same conventions found in cinstern-node-info.json.
    // find our cistern nodes
    String cisternSourceName = "cistern";
    cisternNodes = nodeService.lookUpNodesForSource(cisternSourceName);
    if (cisternNodes == null || cisternNodes.isEmpty()) {
      logger.error("Could not find any " + cisternSourceName + " nodes. Cannot continue");
      throw new RuntimeException("Couldn't find any " + cisternSourceName + " nodes!");
    }
    // check the temps and recirculator pump state for those nodes
    establishTempVariablesAndRecirculatorState();

    if (cisternBottomTemp == null || cisternInletTemp == null || cisternTopTemp == null) {
      logger.error("Could not determine temps to run cistern rule. Will try again later.");
      //return false; //TODO: Startup bug here!
    }

    // find the average temp
    establishAverageTemp();

    // find our current temp delta
    establishTempGain();

    // check to see if there's a manual override set
    SourceOverride solarOverride = OverrideHolder.getSourceOverride(cisternSourceName);
    if (!solarOverride.equals(SourceOverride.RUN)) {
      logger.info("Solar Cistern Manual Override Enabled:" + solarOverride.name());
      if (solarOverride.equals(SourceOverride.OVERRIDE_ON)) {
        return changeRecirculatorState(true,
            "Manual Override: ON. Temperature gain is: " + tempGain);
      } else if (solarOverride.equals(SourceOverride.OVERRIDE_OFF)) {
        return changeRecirculatorState(true, "Manual Override: OFF.");
      }
    }

    if (cisternInletTemp == null) {
      logger.error("Cistern inlet temp is null; cannot continue calculating rules.");
      changeRecirculatorState(false, "Recirculator off: Cistern Inlet Tempature isn't set.");
      return false;
    }

    // max temp check
    if (cisternTopTemp > maxTemp || cisternBottomTemp > maxTemp) { // if it is too hot
      return changeRecirculatorState(false,
          "Recirculator off: Cistern's tempature is above the maxium allowed. Current temp: "
              + cisternTopTemp + ". Max temp allowed: " + maxTemp);
    }

    // daylight check
    if (!DaylightService.getDayLight().isDaylight()) {// if it's night
      return changeRecirculatorState(false, "Recirculator off: Night.");
    }
    long timeSinceRecirculatorLastRan = timeSinceRecirculatorLastRan();
    logger.debug(
        "Current temp gain is: " + tempGain + " the recirculatorPump is: " + recirculatorState
            + " the last time the recirculatorPump ran was: " + timeSinceRecirculatorLastRan);
    // temp gain check
    // if we're not gaining temp and the recirculator is on
    if (tempGain < 0 && recirculatorState) {
      try {
        Thread.sleep(maxColdRuntime);// give it some time to see if it gets better
      } catch (InterruptedException e) {
        ;
      }
      establishTempGain();
      // if it's still not better
      if (tempGain < 0) {
        // turn it off
        return changeRecirculatorState(false,
            "Recirculator off: Not enough incoming heat. " + tempGain);
      }
    } else if (!recirculatorState && timeSinceRecirculatorLastRan > retryTime) {
      // if the recirculator is/was off AND we've passed our retrytime
      // let's hope things are better now; give it a shot!
      return changeRecirculatorState(true,
          "Running to see if we have more heat. Temperature gain is: " + tempGain);
    }

    return changeRecirculatorState(true, "Temperature gain is: " + tempGain);
  }

  private boolean changeRecirculatorState(boolean state, String reason) {
    logger.info("Cistern recirculator is: " + state + " because: " + reason);
    MiscNotices.setCisternNotice(reason);
    if (recirculatorState) {// if the recirculator is currently running
      // let us know that it's still running for future calls
      lastRecirculatorRunTime = new Date().getTime();
    }
    try {
      // Change the recirculatorPump state
      nodeService.changeSourceState("recirculatorPump", state);
    } catch (NodeConnectionException e) {
      logger.error("Cannot connect to node to change recirculatorPump state to " + state, e);
      return false;
    }
    return true;
  }

  /**
   * Get how long it's been since we checked the temp last
   * 
   * @return
   */
  private long timeSinceRecirculatorLastRan() {
    return new Date().getTime() - lastRecirculatorRunTime;
  }

  /**
   * Sets the average temp variable.
   */
  private void establishAverageTemp() {
    // if we don't have one, set it to the other
    if (cisternBottomTemp == null || cisternTopTemp == null) {
      if (cisternBottomTemp == null) {
        cisternAverageTemp = cisternTopTemp;
      } else {
        cisternAverageTemp = cisternBottomTemp;
      }
    } else { // we have both top and bottom
      cisternAverageTemp = (cisternTopTemp + cisternBottomTemp) / 2;
    }
  }

  /**
   * Sets the temp gain variable
   */
  private void establishTempGain() {
    if (cisternTopTemp != null) {
      tempGain = cisternInletTemp - getCisternAverageTemp();
    }
  }


  private void establishTempVariablesAndRecirculatorState() {

    ArrayList<NodeInformation> currentNodes =
        new ArrayList<>(CurrentNodeState.getCurrentNodeState().values());
    ArrayList<NodeInformation> cisternNodeInformation = new ArrayList<>();
    // ^^ note, pulls from cache rather than reloading
    for (NodeInformation ni : currentNodes) {// this could be more efficient, but it's cold outside
      for (Node n : cisternNodes) {
        if (ni.getName().equals(n.getName())) {
          cisternNodeInformation.add(ni);
        }
      }
    }
    // this could be more efficient as well
    for (NodeInformation cni : cisternNodeInformation) {
      for (NodeSourceInformation s : cni.getSources()) {
        Double temp = s.getTemp();
        if (temp != null) { // if it's null, it means the sensor errored, go with the last known
                            // good reading.
          switch (s.getName()) {
            case "cisternInlet":
              cisternInletTemp = temp;
              break;
            case "cisternTop":
              cisternTopTemp = temp;
              break;
            case "cisternBottom":
              cisternBottomTemp = temp;
              break;
            default:
              break;
          }
        } else if (s.getName().equals("recirculatorPump")) { // as long as we're looping through
                                                             // everything, find out what the
                                                             // recirculator is currently doing
          recirculatorState = s.getState();
        }
      }
    }
    // assuming we found everything at this point... probably not a good assumption
    logger.info("Cistern temps: inlet: " + cisternInletTemp + " top: " + cisternTopTemp
        + " bottom: " + cisternBottomTemp);
    logger.info("Recirculator is currently: " + recirculatorState);
  }

  /**
   * Temp at cistern inlet from panels.
   * 
   * @return the cisternInletTemp
   */
  public double getCisternInletTemp() {
    return cisternInletTemp;
  }

  /**
   * Temp at cistern top.
   * 
   * @return the cisternTopTemp
   */
  public double getCisternTopTemp() {
    return cisternTopTemp;
  }

  /**
   * Temp at cistern bottom.
   * 
   * @return the cisternBottomTemp
   */
  public double getCisternBottomTemp() {
    return cisternBottomTemp;
  }

  /**
   * Average temp between both sensors in the cistern.
   * 
   * @return the cisternAverageTemp
   */
  public Double getCisternAverageTemp() {
    return cisternAverageTemp;
  }

  /**
   * Current temp gain from panels to cistern.
   * 
   * @return the tempGain
   */
  public Double getTempGain() {
    return tempGain;
  }

}
