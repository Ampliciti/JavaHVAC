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
package com.ampliciti.javahvac.rules;

import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.domain.config.Region;
import com.ampliciti.javahvac.domain.config.Source;
import com.ampliciti.javahvac.domain.config.Zone;
import com.ampliciti.javahvac.rules.impl.HeatSourceRule;
import com.ampliciti.javahvac.rules.impl.RegionRule;
import com.ampliciti.javahvac.rules.impl.SolarCisternRule;
import com.ampliciti.javahvac.rules.impl.ZoneRule;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * Helper class to generate our rules upon application startup.
 *
 * @author jeffrey
 */
public class RuleGenerator {

  /**
   * Logger for this class.
   */
  private static Logger logger = Logger.getLogger(RuleGenerator.class);

  /**
   * Call this to generate the needed managed rules for the whole application. Note: Server
   * configuration must be established before calling this method.
   *
   * @return Rules to use.
   */
  public static ArrayList<Rule> generateManagedRules() {
    ArrayList<Rule> rules = new ArrayList<>();
    // first define any solar cistern rules -- we want that to go into effect first
    SolarCisternRule scr = generateSolarCisternRule();
    if (scr != null) {
      rules.add(scr);
    }
    // second, define sources rules, so the regions/zones can use them if needed.
    rules.addAll(generateSourcesRules());
    // third, add all our region rules
    rules.addAll(generateRegionRules());
    return rules;
  }

  /**
   * Gets the rules for the managed regions.
   * 
   * @return
   */
  private static ArrayList<RegionRule> generateRegionRules() {
    ArrayList<RegionRule> toReturn = new ArrayList<>();
    for (Region r : ServerConfig.getRegions()) {
      if (r.isManaged()) {
        toReturn.add(new RegionRule(r));
      }
    }
    return toReturn;
  }

  /**
   * Gets the rules for non-managed zones.
   * 
   * @return
   */
  public static ArrayList<Rule> generateNonManagedZoneRules() {
    ArrayList<Rule> toReturn = new ArrayList<>();
    for (Region r : ServerConfig.getRegions()) {
      if (!r.isManaged()) {
        for (Zone z : r.getZones()) {
          toReturn.add(new ZoneRule(z));
        }
      }
    }
    return toReturn;
  }

  /**
   * Gets a list of heat source rules
   * 
   * @return
   */
  private static ArrayList<HeatSourceRule> generateSourcesRules() {
    ArrayList<HeatSourceRule> toReturn = new ArrayList<>();
    for (Source s : ServerConfig.getSources()) {
      toReturn.add(new HeatSourceRule(s));
    }
    if (toReturn.isEmpty()) {
      logger.warn("Warning: No Heat sources!");
    }
    return toReturn;
  }

  /**
   * Generates a single solar cistern rule. Will return null if no cistern exists in the
   * configuration.
   *
   * @return
   */
  private static SolarCisternRule generateSolarCisternRule() {
    for (Source s : ServerConfig.getSources()) {
      if (s.getDetail() != null && s.getDetail().equals(Source.Detail.SOLAR)) {
        return new SolarCisternRule(s.getName(), s.getMax_temp());
      }
    }
    logger.info("No solar cistern config found.");
    return null;
  }
}
