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

import com.ampliciti.javahvac.domain.config.Node;
import java.util.ArrayList;

/**
 * Used for self reporting from Nodes
 * 
 * @author jeffrey
 */
public class NodeInformation extends Node {

  /**
   * Self-reported zone information from this node.
   */
  private ArrayList<NodeZoneInformation> zones;

  /**
   * Get self-reported zone information from this node.
   * 
   * @return
   */
  public ArrayList<NodeZoneInformation> getZones() {
    return zones;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(" Node Zone Information:");
    for (NodeZoneInformation nzi : zones) {
      sb.append(", ");
      sb.append(nzi.toString());
    }
    return sb.toString();
  }

}
