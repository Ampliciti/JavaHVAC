/*
 * Copyright (C) 2018 jeffrey
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
package com.ampliciti.javahvac.domain.config;

/**
 * Location of the building complex.
 * 
 * @author jeffrey
 */
public class Location {


  /**
   * Latitude for this building complex.
   */
  private Double latitude;

  /**
   * Longitude for this building complex.
   */
  private Double longitude;

  /**
   * Constructor.
   * 
   * @param latitude Latitude for this building complex.
   * @param longitude Longitude for this building complex.
   */
  public Location(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }



  /**
   * Default constructor.
   */
  public Location() {}

  /**
   * Latitude for this building complex.
   * 
   * @return the latitude
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * Latitude for this building complex.
   * 
   * @param latitude the latitude to set
   */
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  /**
   * Longitude for this building complex.
   * 
   * @return the longitude
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * Longitude for this building complex.
   * 
   * @param longitude the longitude to set
   */
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return "Location{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
  }



}
