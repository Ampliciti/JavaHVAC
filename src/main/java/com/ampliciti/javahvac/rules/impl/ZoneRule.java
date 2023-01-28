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
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.domain.config.Zone;
import com.ampliciti.javahvac.rules.Rule;

/**
 * Rule for maintaining zones.
 *
 * @author jeffrey
 */
public class ZoneRule implements Rule {

  /**
   * Zone that this rule is for.
   */
  private Zone zone;

  /**
   * Constructor.
   *
   * @param zone Zone that this rule is for.
   */
  public ZoneRule(Zone zone) {
    this.zone = zone;
  }

  @Override
  public boolean enforceRule() {
    if (isInManualMode()) {
      // TODO: Set value to what DB says
    } else {
      // TODO:
      // -- check current zone tempature
      // -- determine if the zone should be running
      // -- ensure the heat source is running
    }
    return true;
  }

  private boolean isInManualMode() {
    if (!zone.isManualAllowed()) {
      return false;
    } else {
      // TODO: check db
      return true;
    }
  }

  @Override
  public String getDefinition() {
    StringBuilder sb = new StringBuilder("Rule for zone: ");
    sb.append(getZone().getName());
    return sb.toString();
  }

  /**
   * @return the zone
   */
  public Zone getZone() {
    return zone;
  }

}
