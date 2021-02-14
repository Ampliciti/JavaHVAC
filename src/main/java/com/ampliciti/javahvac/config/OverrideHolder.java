/*
 * Copyright (C) 2020 jeffrey
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
package com.ampliciti.javahvac.config;

import com.ampliciti.javahvac.dao.domain.SourceOverride;
import com.ampliciti.javahvac.domain.OverrideState;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * This class holds our static overrides for the entire application.
 * 
 * @author jeffrey
 */
public class OverrideHolder {

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(OverrideHolder.class);

  /**
   * Map for source overrides.
   */
  private static ConcurrentHashMap<String, SourceOverride> sourceOverrideMap =
      new ConcurrentHashMap<>();

  /**
   * Fetches the override value for a source. Will return SourceOverride.RUN if there has been no
   * override set.
   * 
   * @param source Source to get the override value for.
   * @return The override value.
   */
  public static SourceOverride getSourceOverride(String source) {
    SourceOverride so = sourceOverrideMap.get(source);
    if (so == null) {
      return SourceOverride.RUN;
    } else {
      return so;
    }
  }

  public static void setSourceOverride(String source, SourceOverride override) {
    SourceOverride existing = sourceOverrideMap.get(source);
    String currentValue;
    if (existing == null) {
      currentValue = "not set";
    } else {
      currentValue = existing.name();
    }
    sourceOverrideMap.put(source, override);
    logger.info("Overriding source: " + source + " to " + override.name()
        + ". Override state was previously: " + currentValue);
  }

  /**
   * Gets all our current overrides as a Properties object for easy printing. This method is
   * probably not 100% Thread Safe, but likely good enough for our needs. Will not print any
   * Overrides in the RUN state, as they're not actually overrides at that point.
   * 
   * @return
   */
  public static List<OverrideState> getAllOverrides() {
    List<OverrideState> toReturn = new ArrayList<>();
    Set<String> keySet = sourceOverrideMap.keySet();
    keySet.forEach(key -> {
      SourceOverride so = sourceOverrideMap.get(key);
      if (so != null && !so.equals(SourceOverride.RUN)) {
        OverrideState os = new OverrideState(key, so);
        toReturn.add(os);
      }
    });
    return toReturn;
  }

  /**
   * Clears <b>all</b> existing overrides and resets everything back into a non-override state.
   * Likely used mostly for unit testing.
   */
  public static void clearOverrides() {
    sourceOverrideMap = new ConcurrentHashMap<>();
  }

}
