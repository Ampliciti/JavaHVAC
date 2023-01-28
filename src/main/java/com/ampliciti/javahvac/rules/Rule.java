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
package com.ampliciti.javahvac.rules;

/**
 * Defines a rule that needs to be enforced in the real world.
 *
 * @author jeffrey
 */
public interface Rule {

  /**
   * Friendly string that defines what this rule is.
   *
   * @return
   */
  String getDefinition();

  /**
   * A call to this method indicates that this rule should be actively enforced in the real world.
   *
   * @return True if the rule was successfully implemented, false if the condition could not be enforced.
   */
  boolean enforceRule();

}
