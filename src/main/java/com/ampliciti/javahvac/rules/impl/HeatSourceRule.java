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
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.domain.Source;
import com.ampliciti.javahvac.rules.Rule;

/**
 * Rule for heat sources such as AC units, or heaters.
 *
 * @author jeffrey
 */
public class HeatSourceRule implements Rule {

  /**
   * Heat source we're dealing with.
   */
  private Source source;

  /**
   * If the associated region is calling for the heat source.
   */
  private boolean calling;

  public HeatSourceRule(Source source) {
    this.source = source;
  }

  @Override
  public boolean enforceRule() {
    if (calling) {
      // TODO: make rest call to turn on resource
    } else {
      // TODO: make rest call to turn off resource
    }
    return true;
  }

  @Override
  public String getDefinition() {
    StringBuilder sb = new StringBuilder("Rule for heat source: ");
    sb.append(source.getName());
    sb.append(" for regions:");
    for (String r : source.getRegions_served()) {
      sb.append(" ");
      sb.append(r);
    }
    return sb.toString();
  }

  /**
   * @return the calling
   */
  public boolean isCalling() {
    return calling;
  }

  /**
   * @param calling the calling to set
   */
  public void setCalling(boolean calling) {
    this.calling = calling;
  }

}
