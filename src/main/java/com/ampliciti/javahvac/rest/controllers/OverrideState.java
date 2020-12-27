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
package com.ampliciti.javahvac.rest.controllers;

import com.ampliciti.javahvac.dao.domain.SourceOverride;

/**
 * State of override for a specific source/zone/etc. Also POJO for request.
 */
public class OverrideState {

  /**
   * Name of source to override.
   */
  private String name;
  /**
   * State to change the named source to.
   */
  private SourceOverride state;

  /**
   * Default constructor; needed for serialization.
   */
  public OverrideState() {}

  /**
   *
   * @param name Name of source to change.
   * @param state State to change the named source to.
   */
  public OverrideState(String name, SourceOverride state) {
    this.name = name;
    this.state = state;
  }

  /**
   * Name of source to change.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Name of source to change.
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * State to change the named source to.
   *
   * @return the state
   */
  public SourceOverride getState() {
    return state;
  }

  /**
   * State to change the named source to.
   *
   * @param state the state to set
   */
  public void setState(SourceOverride state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "OverrideState{" + "name=" + name + ", state=" + state + '}';
  }

}
