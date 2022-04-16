/*
 * Copyright (C) 2019-2022 jeffrey
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
package com.ampliciti.javahvac.service;

import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.dao.domain.DayLight;
import com.ampliciti.javahvac.dao.impl.DayLightDao;
import com.ampliciti.javahvac.exceptions.RESTException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Service for determining if it currently light outside. Location must be populated in the YAML
 * file to make sure this works
 * 
 * @author jeffrey
 */
public class DaylightService {

  /**
   * Last time we were able to contact the sun api.
   */
  private static long lastUpdated = 0l;
  /**
   * The cached daylight object.
   */
  private static DayLight daylight;

  /**
   * Logger for this class.
   */
  private static Logger logger = Logger.getLogger(DaylightService.class);

  /**
   * Method to get the current daylight within the time range of four hours or so. Uses cached
   * values when possible.
   * 
   * @return a populated daylight object.
   * 
   */
  public static synchronized DayLight getDayLight(){
      return getDayLight(0);
  }
  
  /**
   * Method to get the current daylight within the time range of four hours or so. Uses cached
   * values when possible.
   * 
   * @param retryCount Number of times that this call has been retried.
   * 
   * @return a populated daylight object.
   * 
   */
  private static synchronized DayLight getDayLight(int retryCount) {
    if (daylight == null || lastUpdated < new Date().getTime() - (3600000 * 4) || retryCount == 5) {// daylight is not
      // populated, or its out
      // of date.
      DayLightDao dao = new DayLightDao();
      try {
        daylight = dao.getDayLight(ServerConfig.getLocation());// fetch from API
        lastUpdated = new Date().getTime();// update the last updated time to now
        logger.debug("Successfully updated DayLight cache.");
      } catch (RESTException | RuntimeException e) {
        logger.warn("Warning: Could not contact/understand API for determining daylight.", e);
        if (daylight == null) { // we can't just return the cached value, because we don't have one.
          try {
            Thread.sleep(10000);// wait 10 seconds
            return getDayLight(++retryCount);// recurse (to try again) -- not stictly safe, but it's cold
                                 // outside and I need this working.
          } catch (InterruptedException ie) {
            ;// don't care
          }
        } else {
          return daylight;// use our last known value
        }

      }
    }
    logger.debug("Current daylight is: " + daylight.toString());
    return daylight;
  }

  /**
   * Last time we were able to contact the sun api.
   * 
   * @return the lastUpdated
   */
  public static long getLastUpdated() {
    return lastUpdated;
  }

}
