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
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.Utils;
import com.ampliciti.javahvac.dao.NodeCommandDao;
import com.ampliciti.javahvac.dao.RESTDao;
import com.ampliciti.javahvac.exceptions.NodeConnectionException;
import com.ampliciti.javahvac.exceptions.RESTException;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Dao for sending commands to a node telling them to take action on something via REST.
 * 
 * @author jeffrey
 */
public class NodeCommandRESTDao implements NodeCommandDao {

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(NodeInformationRESTDao.class);

  /**
   * Constructor.
   */
  public NodeCommandRESTDao() {}

  /**
   * Sends a command to the specified node via REST.
   * 
   * @param nodeAddress Address to the node to send the command to.
   * @param name Zone or source to change the state of.
   * @param command Command to send. True means turn the name on, false means turn it off.
   * @return true if the command was executed successfully, false if it failed for some reason.
   * @throws NodeConnectionException if there's a problem connecting to the node.
   */
  @Override
  public boolean sendCommand(String nodeAddress, String name, boolean command)
      throws NodeConnectionException {
    RESTDao restDao = new RESTDaoImpl(Utils.buildUrlFromAddressString(nodeAddress));
    JSONAware restResponse = null;
    try {
      Map mapToPost = new HashMap();
      mapToPost.put("name", name);
      mapToPost.put("state", command);
      JSONObject toPost = new JSONObject(mapToPost);
      restResponse = restDao.doPostCall("/action", toPost);
      String jsonResponse = restResponse.toJSONString();
      logger.debug("Response from node: " + nodeAddress + " is: " + jsonResponse);
      if (jsonResponse != null && jsonResponse.contains(name)
          && jsonResponse.contains(Boolean.toString(command))) { // quick/lazy
        return true;
      } else {
        return false;
      }
    } catch (RESTException e) {
      throw new NodeConnectionException("Could not connect to node at: " + nodeAddress, e);
    } catch (JsonSyntaxException e) {
      if (restResponse != null) {
        throw new NodeConnectionException("Was able to connect to node at: " + nodeAddress
            + ", however, the response was not in the expected format: "
            + restResponse.toJSONString(), e);
      } else {
        throw new NodeConnectionException("Was able to connect to node at: " + nodeAddress
            + ", however, no response was returned.", e);
      }
    }
  }



}
