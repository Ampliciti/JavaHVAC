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
import com.ampliciti.javahvac.dao.NodeInformationDao;
import com.ampliciti.javahvac.dao.RESTDao;
import com.ampliciti.javahvac.dao.exception.NodeConnectionException;
import com.ampliciti.javahvac.domain.NodeInformation;
import com.ampliciti.javahvac.exceptions.RESTException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * REST Dao for getting self-reported information from nodes.
 * 
 * @author jeffrey
 */
public class NodeInformationRESTDao implements NodeInformationDao {

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(NodeInformationRESTDao.class);

  /**
   * Constructor.
   */
  public NodeInformationRESTDao() {}

  /**
   * Gets self reported node information from a single node.
   * 
   * @param nodeAddress Address to find the node on.
   * @return
   */
  @Override
  public NodeInformation getInfo(String nodeAddress) throws NodeConnectionException {
    Gson gson = new Gson();
    RESTDao restDao = new RESTDaoImpl(Utils.buildUrlFromAddressString(nodeAddress));
    JSONObject restResponse = null;
    try {
      restResponse = restDao.doGetCall("/info");
      logger.debug("Response from node: " + nodeAddress + " is: " + restResponse.toJSONString());
      return gson.fromJson(restResponse.toJSONString(), NodeInformation.class);
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
