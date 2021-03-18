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
package com.ampliciti.javahvac.dao.domain;

/**
 * Enum used to determine if the system (heat source or region) needs to be in an overridden state.
 * 
 * @author jeffrey
 */
public enum OverrideEnum {
  /**
   * Indicates that the system should be overridden in the ON position.
   */
  OVERRIDE_ON,
  /**
   * Indicates that the system should be overridden in the OFF position.
   */
  OVERRIDE_OFF,
  /**
   * Indicates that the system should run as normal (be controlled by the rules defined).
   */
  RUN;

}
