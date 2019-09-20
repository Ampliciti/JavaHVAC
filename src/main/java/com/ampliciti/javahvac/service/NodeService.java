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
import com.ampliciti.javahvac.dao.NodeCommandDao;
import com.ampliciti.javahvac.dao.NodeInformationDao;
import com.ampliciti.javahvac.dao.impl.NodeCommandRESTDao;
import com.ampliciti.javahvac.exceptions.NodeConnectionException;
import com.ampliciti.javahvac.dao.impl.NodeInformationRESTDao;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.config.Node;
import com.ampliciti.javahvac.domain.NodeInformation;
import com.ampliciti.javahvac.domain.NodeZoneInformation;
import com.ampliciti.javahvac.domain.config.Region;
import com.ampliciti.javahvac.domain.config.Zone;
import com.ampliciti.javahvac.exceptions.PermissionsException;
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
  private static Logger logger = Logger.getLogger(NodeService.class);

  private NodeCommandDao nodeCommander;
  private NodeInformationDao nodeInformation;

  /**
   * Default constructor.
   */
  public NodeService() {
    nodeCommander = new NodeCommandRESTDao();
    nodeInformation = new NodeInformationRESTDao();
  }

  /**
   * Checks that we can connect to our nodes and that they are up and running. Used as a basic
   * application startup check.
   * 
   * @return True if we can talk to ALL nodes, false if one or more node is down.
   */
  public boolean checkNodeConnections() {
    boolean connectedToAll = true;
    ArrayList<Node> allNodes = ServerConfig.getNodes();

    for (Node node : allNodes) {
      logger.info(
          "Attempting to connect to node: " + node.getName() + " on address: " + node.getAddress());
      try {
        NodeInformation ni = nodeInformation.getInfo(node.getAddress());
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

  /**
   * Method that gets called when a zone needs to change state.
   * 
   * @param zoneName Name of the zone to change.
   * @param command State to change it to.
   * @return True if the state was changed successfully. False if there was a problem.
   * @throws NodeConnectionException If the node is unreachable.
   */
  public boolean changeZoneState(String zoneName, boolean command) throws NodeConnectionException {
    Node nodeforZone = lookUpNodeForZone(zoneName);
    if (nodeforZone == null) {
      throw new NodeConnectionException("No node for that zone could be found.");
    }
    return nodeCommander.sendCommand(nodeforZone.getAddress(), zoneName, command);

  }

  /**
   * Method that gets called when an end user wants to change a Zone state.
   * 
   * @param zoneName Name of the zone to change.
   * @param command State to change it to.
   * @return True if the state was changed successfully. False if there was a problem.
   * @throws NodeConnectionException If the node is unreachable.
   * @throws PermissionsException if the user isn't allowed to set this zone manually per
   *         configuration rules.
   */
  public boolean endUserChangeZoneState(String zoneName, boolean command)
      throws NodeConnectionException, PermissionsException {
    // check permissions
    Zone zone = lookupZoneInRegion(zoneName);
    if (zone == null) {// doesn't exist in config
      throw new NodeConnectionException("Zone: " + zoneName + " does not exist in configuration.");
    } else if (zone.isManualAllowed()) {// all ok
      return changeZoneState(zoneName, command);
    } else {// no manual control allowed.
      throw new PermissionsException("Manual control on zone: " + zoneName + " is not allowed.");
    }
  }

  /**
   * Helper method that looks up the Node that controls a Zone.
   * 
   * @param zoneName
   * @return The node if a zone exists. Null otherwise.
   */
  private Node lookUpNodeForZone(String zoneName) {
    ArrayList<NodeInformation> currentNodes =
        new ArrayList<>(CurrentNodeState.getCurrentNodeState().values());
    // ^^ note, pulls from cache rather than reloading
    for (NodeInformation ni : currentNodes) {
      for (NodeZoneInformation nzi : ni.getZones())
        if (nzi.getName().equals(zoneName)) { // hey, this node controls this zone
          ArrayList<Node> serverConfigNodes = ServerConfig.getNodes();
          // ^^ lets pull the nodes from ServerConfig as we know them
          // and find the Node object we want
          for (Node n : serverConfigNodes) {
            if (n.getName().equals(ni.getName())) {
              return n;// and return it from there instead: it will have the right DNS/IP and port,
                       // rather than relying on the self reported address
            }
          }
          logger.error("Node found for zone: " + zoneName + ", but it was not in the ServerConfig");
        }
    }
    return null; // node not found for this zone
  }

  /**
   * Looks up a zone by zone name.
   * 
   * @param zone
   * @return
   */
  private Zone lookupZoneInRegion(String zone) {
    for (Region r : ServerConfig.getRegions()) {
      for (Zone z : r.getZones()) {
        if (z.getName().equals(zone)) {
          return z;
        }
      }
    }
    return null;
  }

}
