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
package com.ampliciti.javahvac.domain;

/**
 * Zone information that is being self-reported from the nodes.
 * 
 * @author jeffrey
 */
public class NodeSourceInformation {

  /**
   * Name of the source this node is reporting information for.
   */
  private String source;

  /**
   * Current state of the zones relay/switch. True = switch on, False = switch off. Using objects
   * rather than primitives here to allow for null values.
   */
  private Boolean state;

  /**
   * Name of the specific function of the source that this node is reporting for.
   */
  private String name;


  /**
   * Current temperature that the node is reporting for this zone. Using objects rather than
   * primitives here to allow for null values. For sources, this will frequently be null.
   */
  private Double temp;

  /**
   * Constructor.
   * 
   * @param source Name of the source this node is reporting information for.
   * 
   * @param name Name of the specific function of the source that this node is reporting for.
   * @param state Current state of the zones relay/switch. True = switch on, False = switch off.
   *        Using objects rather than primitives here to allow for null values.
   * @param temp Current temperature that the node is reporting for this zone. Using objects rather
   *        than primitives here to allow for null values.
   */
  public NodeSourceInformation(String source, String name, Boolean state, Double temp) {
    this.name = name;
    this.source = source;
    this.state = state;
    this.temp = temp;
  }

  /**
   * @return the Name of the zone that this node is reporting for.
   */
  public String getName() {
    return name;
  }

  /**
   * Current temperature that the node is reporting for this zone. Using objects rather than
   * primitives here to allow for null values.
   * 
   * @return
   */
  public Boolean getState() {
    return state;
  }

  /**
   * Name of the source this node is reporting information for.
   * 
   * @return the source
   */
  public String getSource() {
    return source;
  }

  /**
   * Current temperature that the node is reporting for this zone. Using objects rather than
   * primitives here to allow for null values. For sources, this will frequently be null.
   * 
   * @return the temp
   */
  public Double getTemp() {
    return temp;
  }

  @Override
  public String toString() {
    return "NodeSourceInformation{" + "source=" + source + ", state=" + state + ", name=" + name
        + ", temp=" + temp + '}';
  }



}
