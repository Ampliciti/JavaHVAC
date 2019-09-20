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

import com.ampliciti.javahvac.dao.NodeCommandDao;
import com.ampliciti.javahvac.dao.exception.NodeConnectionException;
import org.apache.log4j.Logger;

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
   * @param address Address to the node to send the command to.
   * @param zone Zone to change the state of.
   * @param command Command to send. True means turn the zone on, false means turn it off.
   * @return true if the command was executed successfully, false if it failed for some reason.
   * @throws NodeConnectionException if there's a problem connecting to the node.
   */
  @Override
  public boolean sendCommand(String address, String zone, boolean command)
      throws NodeConnectionException {
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated
                                                                   // methods, choose Tools |
                                                                   // Templates.
  }



}
