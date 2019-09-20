/*
 * Copyright (C) 2018-2019 jeffrey
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
package com.ampliciti.javahvac.service;

import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.dao.NodeInformationDao;
import com.ampliciti.javahvac.dao.exception.NodeConnectionException;
import com.ampliciti.javahvac.dao.impl.NodeInformationRESTDao;
import com.ampliciti.javahvac.domain.config.Node;
import com.ampliciti.javahvac.domain.NodeInformation;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * Service class for interacting with nodes.
 * 
 * @author jeffrey
 */
public class NodeService {

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(NodeService.class);

  // TODO: Consider making methods statically accessable.

  /**
   * Default constructor.
   */
  public NodeService() {}

  /**
   * Checks that we can connect to our nodes and that they are up and running. Used as a basic
   * application startup check.
   * 
   * @return True if we can talk to ALL nodes, false if one or more node is down.
   */
  public boolean checkNodeConnections() {
    boolean connectedToAll = true;
    ArrayList<Node> allNodes = ServerConfig.getNodes();
    NodeInformationDao nodeDao = new NodeInformationRESTDao();
    for (Node node : allNodes) {
      logger.info(
          "Attempting to connect to node: " + node.getName() + " on address: " + node.getAddress());
      try {
        NodeInformation ni = nodeDao.getInfo(node.getAddress());
        logger.info(
            "Successfully connected to node: " + node.getName() + ", Response: " + ni.toString());
      } catch (NodeConnectionException e) {
        logger.error("Could not connect to node " + node.getName() + " on address: "
            + node.getAddress() + " to confirm connection.", e);
        connectedToAll = false;
        // even though we've failed here, we'll keep checking the rest of the nodes for logging
        // purposes.
      }
    }
    return connectedToAll;
  }

  /**
   * Pulls the state from all nodes that it's presently able to talk to.
   */
  public ConcurrentHashMap<String, NodeInformation> pullNodeState() {
    ArrayList<Node> allNodes = ServerConfig.getNodes();
    ConcurrentHashMap<String, NodeInformation> toReturn = new ConcurrentHashMap<>();
    for (Node node : allNodes) {
      NodeInformation ni = pullNodeState(node);
      if (ni != null) {
        toReturn.put(node.getName(), ni);
      }
    }
    return toReturn;
  }

  /**
   * Pulls the state from a single node.
   * 
   * @param node Node to pull state from/for.
   * @return NodeInformation if it can get it. Null if it's unreachable or not available.
   */
  public NodeInformation pullNodeState(Node node) {
    NodeInformationDao nodeDao = new NodeInformationRESTDao();
    logger.info(
        "Attempting to connect to node: " + node.getName() + " on address: " + node.getAddress());
    try {
      NodeInformation ni = nodeDao.getInfo(node.getAddress());
      logger.info(
          "Successfully connected to node: " + node.getName() + ", Response: " + ni.toString());
      return ni;
    } catch (NodeConnectionException e) {
      logger.error(
          "Could not connect to node " + node.getName() + " on address: " + node.getAddress(), e);
      return null;
    }

  }

}
