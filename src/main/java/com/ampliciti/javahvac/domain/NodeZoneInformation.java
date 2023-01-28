/*
 * Copyright (C) 2019 jeffrey
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
package com.ampliciti.javahvac.domain;

/**
 * Zone information that is being self-reported from the nodes.
 * 
 * @author jeffrey
 */
public class NodeZoneInformation {

  /**
   * Current temperature that the node is reporting for this zone. Using objects rather than primitives here to allow
   * for null values.
   */
  private Double temp;

  /**
   * Current state of the zones relay/switch. True = switch on, False = switch off. Using objects rather than primitives
   * here to allow for null values.
   */
  private Boolean state;

  /**
   * Name of the zone that this node is reporting for.
   */
  private String name;

  /**
   * Constructor.
   * 
   * @param name Name of the zone that this node is reporting for.
   * 
   * @param temp Current temperature that the node is reporting for this zone. Using objects rather than primitives here
   *        to allow for null values.
   * @param state Current state of the zones relay/switch. True = switch on, False = switch off. Using objects rather
   *        than primitives here to allow for null values.
   */
  public NodeZoneInformation(String name, Double temp, Boolean state) {
    this.name = name;
    this.temp = temp;
    this.state = state;
  }

  /**
   * @return the Name of the zone that this node is reporting for.
   */
  public String getName() {
    return name;
  }

  /**
   * Current temperature that the node is reporting for this zone. Using objects rather than primitives here to allow
   * for null values.
   * 
   * @return
   */
  public Boolean getState() {
    return state;
  }

  /**
   * Current temperature that the node is reporting for this zone. Using objects rather than primitives here to allow
   * for null values.
   * 
   * @return
   */
  public Double getTemp() {
    return temp;
  }



  @Override
  public String toString() {
    return "NodeZoneInformation{" + "temp=" + temp + ", state=" + state + ", name=" + getName() + '}';
  }



}
