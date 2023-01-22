/*
 * Copyright (C) 2018 jeffrey
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
package com.ampliciti.javahvac.domain.config;

/**
 * A zone is associated with region in a many:1 fashion and is controlled by a valve or switch. Each
 * region needs at least one zone.
 *
 * @author jeffrey
 */
public class Zone {

  /**
   * Name of zone. Examples of zone names: "Entrance", "Hall", "Living Room South"
   */
  private String name;

  /**
   * Flag indicating if manual control/override of this zone by the end user is permitted. Defaults
   * to false.
   */
  private boolean manualAllowed = false;

  /**
   * If present, this string must match another zonename exactly. When that zone is activated, this
   * zone will be activated as well.
   */
  private String syncWith;

  /**
   * Runtime for this zone in seconds. This is used to allow regions to round robin heat sources
   * between zones. A time of zero indicates that the zone should never be automatically started. If
   * the region desires a climate change, the zone will run for this amount of time, then turn off,
   * allowing another zone to run. If there is only one zone in region, this value is ignored.
   *
   * Defaults to 0;
   */
  private int runtime = 0;

  /**
   * Default constructor.
   */
  public Zone() {}

  /**
   * Name of zone. Examples of zone names: "Entrance", "Hall", "Living Room South"
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Flag indicating if manual control/override of this zone by the end user is permitted.
   *
   * @return the manualAllowed
   */
  public boolean isManualAllowed() {
    return manualAllowed;
  }

  /**
   * Runtime for this zone in seconds. This is used to allow regions to round robin heat sources
   * between zones. A time of zero indicates that the zone should never be automatically started. If
   * the region desires a climate change, the zone will run for this amount of time, then turn off,
   * allowing another zone to run. If there is only one zone in region, this value is ignored.
   *
   * @return the runtime in minutes.
   */
  public int getRuntime() {
    return runtime;
  }

  /**
   * If present, this string must match another zonename exactly. When that zone is activated, this
   * zone will be activated as well.
   * 
   * @return the syncWith
   */
  public String getSyncWith() {
    return syncWith;
  }

  /**
   * If present, this string must match another zonename exactly. When that zone is activated, this
   * zone will be activated as well.
   * 
   * @param syncWith the syncWith to set
   */
  public void setSyncWith(String syncWith) {
    this.syncWith = syncWith;
  }

  @Override
  public String toString() {
    return "Zone{" + "name=" + name + ", manualAllowed=" + manualAllowed + ", runtime=" + runtime
        + '}';
  }

}
