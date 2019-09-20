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
import com.ampliciti.javahvac.service.NodeService;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds the current state of all the nodes in the system, in-memory for rapid access.
 * 
 * @author jeffrey
 */
public class CurrentNodeState {

  /**
   * In memory map for the current node states.
   */
  private static ConcurrentHashMap<String, NodeInformation> currentNodeState;

  public static ConcurrentHashMap<String, NodeInformation> getCurrentNodeState() {
    buildMapIfNotExists();
    return currentNodeState;
  }

  /**
   * Refreshes a single node state.
   * 
   * @param toRefresh
   */
  public static synchronized void refreshSingleNode(Node toRefresh) {
    buildMapIfNotExists();
    NodeService nodeService = new NodeService();
    NodeInformation ni = nodeService.pullNodeState(toRefresh);
    currentNodeState.put(toRefresh.getName(), ni);
  }

  /**
   * Goes out to all the nodes and asks them for their current state, then stores it in this object.
   * Existing objects in the list are removed if they're no longer connected.
   */
  public static synchronized void refreshNodeState() {
    NodeService nodeService = new NodeService();
    currentNodeState = nodeService.pullNodeState();
  }

  /**
   * Creates an (empty) ConcurrentHashMap object if it does not already exist.
   */
  private static void buildMapIfNotExists() {
    if (currentNodeState == null) {
      currentNodeState = new ConcurrentHashMap<>();
    }
  }

}
