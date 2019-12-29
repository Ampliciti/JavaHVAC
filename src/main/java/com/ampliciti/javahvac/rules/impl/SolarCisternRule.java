/*
 * Copyright (C) 2019 jeffrey
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

import com.ampliciti.javahvac.Main;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.MiscNotices;
import com.ampliciti.javahvac.domain.NodeInformation;
import com.ampliciti.javahvac.domain.NodeSourceInformation;
import com.ampliciti.javahvac.domain.config.Node;
import com.ampliciti.javahvac.rules.Rule;
import com.ampliciti.javahvac.service.DaylightService;
import com.ampliciti.javahvac.service.NodeService;
import java.util.ArrayList;
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

  @Override
  public String getDefinition() {
    return "Solar cistern rule for: " + name;
  }

  @Override
  public boolean enforceRule() {
    // NOTE: This is rather specific to my setup. Either override with your own rules, or use the
    // exact same conventions found in cinstern-node-info.json.
    ArrayList<Node> cisternNodes = nodeService.lookUpNodesForSource("cistern");
    establishTempVariables(cisternNodes);
    if (cisternTopTemp > maxTemp || cisternBottomTemp > maxTemp) { // if it is too hot
      changeReciculatorState(false,
          "Reciculator off: Cistern's tempature is above the maxium allowed. Current temp: "
              + cisternTopTemp + ". Max temp allowed: " + maxTemp);
      return true;
    }
    if (!DaylightService.getDayLight().isDaylight()) {// if it's night
      changeReciculatorState(false, "Reciculator off: Not enough light.");
      return true;
    }
    // TODO if there's no reason to have it off, turn it on! Leave message about temp gain/loss.
    return true;
  }

  private void changeReciculatorState(boolean state, String reason) {
    MiscNotices.setCisternNotice(reason);
    // TODO: Find reciculators and turn them off
  }

  private void establishTempVariables(ArrayList<Node> cisternNodes) {

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
        }
      }

    }
    // assuming we found everything at this point... probably not a good assumption
    logger.info("Cistern temps: inlet: " + cisternInletTemp + " top: " + cisternTopTemp
        + " bottom: " + cisternBottomTemp);
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

}
