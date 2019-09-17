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
package com.ampliciti.javahvac.domain.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;

/**
 *
 * @author jeffrey
 */
public class Source {

  /**
   * Type of source; heat, AC or both.
   */
  public enum Type {
    HEAT, AC, BOTH
  }

  /**
   * Detail of the type of source.
   */
  public enum Detail {
    SOLAR
    // etc
  }

  /**
   * Name of this source. Examples: "Main furnace", "cistern", "AC".
   */
  private String name;

  /**
   * Regions that this source is associated with. MUST match actual region names.
   */
  private ArrayList<String> regions_served;

  /**
   * Type of source.
   */
  private Type type;

  /**
   * Detail of the type of source.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Detail detail;

  /**
   * Maximum temp that this source should be allowed to achieve.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private float max_temp;

  /**
   * Name of this source. Examples: "Main furnace", "cistern", "AC".
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Name of this source. Examples: "Main furnace", "cistern", "AC".
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Regions that this source is associated with. MUST match actual region names.
   *
   * @return the regions_served
   */
  public ArrayList<String> getRegions_served() {
    return regions_served;
  }

  /**
   * Regions that this source is associated with. MUST match actual region names.
   *
   * @param regions_served the regions_served to set
   */
  public void setRegions_served(ArrayList<String> regions_served) {
    this.regions_served = regions_served;
  }

  /**
   * Type of source.
   *
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * Type of source.
   *
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * Detail of the type of source.
   *
   * @return the detail
   */
  public Detail getDetail() {
    return detail;
  }

  /**
   * Detail of the type of source.
   *
   * @param detail the detail to set
   */
  public void setDetail(Detail detail) {
    this.detail = detail;
  }

  /**
   * Maximum temp that this source should be allowed to achieve.
   *
   * @return the max_temp
   */
  public float getMax_temp() {
    return max_temp;
  }

  /**
   * Maximum temp that this source should be allowed to achieve.
   *
   * @param max_temp the max_temp to set
   */
  public void setMax_temp(float max_temp) {
    this.max_temp = max_temp;
  }

}
