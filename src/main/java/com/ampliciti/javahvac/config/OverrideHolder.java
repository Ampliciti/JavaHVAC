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
   * Clears all existing overrides and resets everything back into a non-override state. Likely used
   * mostly for unit testing.
   */
  public static void clearOverrides() {
    sourceOverrideMap = new ConcurrentHashMap<>();
  }

}
