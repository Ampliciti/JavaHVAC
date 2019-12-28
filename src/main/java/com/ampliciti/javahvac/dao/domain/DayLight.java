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
package com.ampliciti.javahvac.dao.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Object that stores the sunrise/sunset times and includes helper methods for determining if it it
 * currently within daylight hours.
 * 
 * @author jeffrey
 */
public class DayLight {
  /**
   * Time of sunrise.
   */
  private long sunrise;
  /**
   * Time of sunset.
   */
  private long sunset;

  /**
   * Constructor.
   * 
   * @param sunrise Time of sunrise.
   * @param sunset Time of sunset.
   */
  public DayLight(long sunrise, long sunset) {
    this.sunrise = sunrise;
    this.sunset = sunset;
  }

  /**
   * Time of sunrise.
   * 
   * @return the sunrise
   */
  public long getSunrise() {
    return sunrise;
  }

  /**
   * Time of sunset.
   * 
   * @return the sunset
   */
  public long getSunset() {
    return sunset;
  }

  /**
   * Returns true if it is currently light outside. Assumes the sunset/sunrise times are for today.
   * 
   * @return True if it is light outside, false if it is dark.
   */
  public boolean isDaylight() {
    DateTime nowDateTime = new DateTime(); // Gives the default time zone.
    DateTime dateTime = nowDateTime.toDateTime(DateTimeZone.UTC); // Converting default zone to UTC
    long now = dateTime.getMillis();
    if (now > sunrise && now < sunset) {
      return true;
    }
    return false;
  }

    @Override
    public String toString() {
        return "DayLight{" + "sunrise=" + sunrise + ", sunset=" + sunset + ", isLight="+isDaylight()+"}";
    }
  
  

}
