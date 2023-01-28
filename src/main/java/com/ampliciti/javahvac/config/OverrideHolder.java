/*
 * Copyright (C) 2020-2021 jeffrey
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
package com.ampliciti.javahvac.config;

import com.ampliciti.javahvac.dao.domain.OverrideEnum;
import com.ampliciti.javahvac.domain.OverrideState;
import java.util.ArrayList;
import java.util.List;
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
  private static ConcurrentHashMap<String, OverrideEnum> sourceOverrideMap = new ConcurrentHashMap<>();

  /**
   * Map for region overrides.
   */
  private static ConcurrentHashMap<String, OverrideEnum> regionOverrideMap = new ConcurrentHashMap<>();

  /**
   * Fetches the override value for a source. Will return OverrideEnum.RUN if there has been no override set.
   *
   * @param source Source to get the override value for.
   * @return The override value.
   */
  public static OverrideEnum getSourceOverride(String source) {
    return getOverrideFromMap(sourceOverrideMap, source);
  }

  /**
   * Fetches the override value for a region. Will return OverrideEnum.RUN if there has been no override set.
   *
   * @param region Region to get the override value for.
   * @return The override value.
   */
  public static OverrideEnum getRegionOverride(String region) {
    return getOverrideFromMap(regionOverrideMap, region);
  }

  /**
   * Helper method to pull an override from a map with defaulting.
   * 
   * @param map Map to pull override from
   * @param overrideToFetch Override to pull.
   * @return The override value.
   */
  private static OverrideEnum getOverrideFromMap(ConcurrentHashMap<String, OverrideEnum> map, String overrideToFetch) {
    OverrideEnum override = map.get(overrideToFetch);
    if (override == null) {
      return OverrideEnum.RUN;
    } else {
      return override;
    }
  }

  /**
   * Sets a source override.
   *
   * @param source Source to set the override value for.
   * @param override The override value to be set.
   */
  public static void setSourceOverride(String source, OverrideEnum override) {
    setOverrideToMap(sourceOverrideMap, source, override);
  }

  /**
   * Sets a region override.
   *
   * @param region Region to set the override value for.
   * @param override The override value to be set.
   */
  public static void setRegionOverride(String region, OverrideEnum override) {
    setOverrideToMap(regionOverrideMap, region, override);
  }

  /**
   * Helper method to set an override to a map with logging.
   * 
   * @param map Map to set the override to
   * @param overrideToSet Override to set.
   * @param override The override value.
   */
  private static void setOverrideToMap(ConcurrentHashMap<String, OverrideEnum> map, String overrideToSet,
      OverrideEnum override) {
    OverrideEnum existing = map.get(overrideToSet);
    String currentValue;
    if (existing == null) {
      currentValue = "not set";
    } else {
      currentValue = existing.name();
    }
    map.put(overrideToSet, override);
    logger.info(
        "Overriding: " + overrideToSet + " to " + override.name() + ". Override state was previously: " + currentValue);
  }

  /**
   * Gets all our current source overrides as a Properties object for easy printing. This method is probably not 100%
   * Thread Safe, but likely good enough for our needs. Will not print any Overrides in the RUN state, as they're not
   * actually overrides at that point.
   *
   * @return
   */
  public static List<OverrideState> getAllSourceOverrides() {
    List<OverrideState> toReturn = new ArrayList<>();
    Set<String> keySet = sourceOverrideMap.keySet();
    keySet.forEach(key -> {
      OverrideEnum so = sourceOverrideMap.get(key);
      if (so != null && !so.equals(OverrideEnum.RUN)) {
        OverrideState os = new OverrideState(key, so);
        toReturn.add(os);
      }
    });
    return toReturn;
  }

  /**
   * Gets all our current region overrides as a Properties object for easy printing. This method is probably not 100%
   * Thread Safe, but likely good enough for our needs. Will not print any Overrides in the RUN state, as they're not
   * actually overrides at that point.
   *
   * @return
   */
  public static List<OverrideState> getAllRegionOverrides() {
    List<OverrideState> toReturn = new ArrayList<>();
    Set<String> keySet = regionOverrideMap.keySet();
    keySet.forEach(key -> {
      OverrideEnum so = regionOverrideMap.get(key);
      if (so != null && !so.equals(OverrideEnum.RUN)) {
        OverrideState os = new OverrideState(key, so);
        toReturn.add(os);
      }
    });
    return toReturn;
  }

  /**
   * Gets all our current overrides as a Properties object for easy printing. This method is probably not 100% Thread
   * Safe, but likely good enough for our needs. Will not print any Overrides in the RUN state, as they're not actually
   * overrides at that point.
   *
   * @return
   */
  public static List<OverrideState> getAllOverrides() {
    List<OverrideState> toReturn = new ArrayList<>();
    Set<String> keySet = sourceOverrideMap.keySet();
    keySet.forEach(key -> {
      OverrideEnum so = sourceOverrideMap.get(key);
      if (so != null && !so.equals(OverrideEnum.RUN)) {
        OverrideState os = new OverrideState(key, so);
        toReturn.add(os);
      }
    });
    // probably a better way to do this than copy+paste
    keySet = regionOverrideMap.keySet();
    keySet.forEach(key -> {
      OverrideEnum so = regionOverrideMap.get(key);
      if (so != null && !so.equals(OverrideEnum.RUN)) {
        OverrideState os = new OverrideState(key, so);
        toReturn.add(os);
      }
    });
    return toReturn;
  }

  /**
   * Clears <b>all</b> existing overrides and resets everything back into a non-override state. Likely used mostly for
   * unit testing.
   */
  public static void clearOverrides() {
    sourceOverrideMap = new ConcurrentHashMap<>();
    regionOverrideMap = new ConcurrentHashMap<>();
  }

}
