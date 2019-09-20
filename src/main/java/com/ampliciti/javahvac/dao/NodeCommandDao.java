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
package com.ampliciti.javahvac.dao;

import com.ampliciti.javahvac.exceptions.NodeConnectionException;

/**
 * Dao for sending commands to a node telling them to take action on something.
 * 
 * @author jeffrey
 */
public interface NodeCommandDao {


  /**
   * Sends a command to the specified node.
   * 
   * @param nodeAddress Address to the node to send the command to.
   * @param zone Zone to change the state of.
   * @param command Command to send. True means turn the zone on, false means turn it off.
   * @return true if the command was executed successfully, false if it failed for some reason.
   * @throws NodeConnectionException if there's a problem connecting to the node.
   */
  public boolean sendCommand(String nodeAddress, String zone, boolean command)
      throws NodeConnectionException;



}
