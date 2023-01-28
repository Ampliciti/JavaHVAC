/*
 * Copyright (C) 2019-2021 jeffrey
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.dao.domain.OverrideEnum;
import com.ampliciti.javahvac.domain.OverrideState;
import com.ampliciti.javahvac.domain.config.Region;
import com.ampliciti.javahvac.exceptions.NodeConnectionException;
import com.ampliciti.javahvac.rules.Rule;
import com.ampliciti.javahvac.service.NodeService;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * Rule for maintaining regions.
 * 
 * @author jeffrey
 */
public class RegionRule implements Rule {

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(RegionRule.class);

  /**
   * Node Service for us to use.
   */
  private NodeService nodeService;

  /**
   * Region for this rule.
   */
  private Region region;

  // private final String pumpName;

  public RegionRule(Region region) {
    this.region = region;
    // this.pumpName = region.getName() + "Pump";
    this.nodeService = new NodeService();
  }

  @Override
  public String getDefinition() {
    return "Rule for Region: " + region.getName();
  }

  /**
   * A call to this method indicates that this rule should be actively enforced in the real world.
   *
   * @return True if the rule was successfully implemented, false if the condition could not be enforced.
   */
  @Override
  public synchronized boolean enforceRule() {
    // check for overrides
    ArrayList<OverrideState> regionOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllRegionOverrides();
    for (OverrideState os : regionOverrides) {
      String regionName = region.getName();
      if (os.getName().equals(regionName) && !os.getState().equals(OverrideEnum.RUN)) { // if it's
                                                                                        // for this
                                                                                        // region,
                                                                                        // and not
                                                                                        // set to
                                                                                        // RUN
        logger.info("Found override for region: " + regionName);
        if (os.getState().equals(OverrideEnum.OVERRIDE_ON)) {
          return changeRegionState(true, "Region " + regionName + " Manual Override: ON.");
        } else if (os.getState().equals(OverrideEnum.OVERRIDE_OFF)) {
          return changeRegionState(false, "Region " + regionName + " Manual Override: OFF.");
        } else {
          logger.warn("This should never happen. Logging just in case. Indicates a programming error.");
          return false;
        }
      }
    }
    if (region.isManaged()) {
      // TODO: check db for current temp, and desired temp
      // then act by interating through the zones
    } else {
      // check DB for state that it should be
    }
    return true;
  }

  private boolean changeRegionState(boolean state, String reason) {
    logger.info("Region " + region.getName() + " state is: " + state + " because: " + reason);
    try {
      // Change the pumpName state
      nodeService.changeRegionState(region.getName(), state);
    } catch (NodeConnectionException e) {
      logger.error("Cannot connect to node to change " + region.getName() + " state to " + state, e);
      return false;
    }
    return true;
  }

}
