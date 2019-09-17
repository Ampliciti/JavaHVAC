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

import com.ampliciti.javahvac.dao.exception.NodeConnectionException;
import com.ampliciti.javahvac.domain.NodeInformation;

/**
 * Dao for getting self-reported information from nodes.
 * 
 * @author jeffrey
 */
public interface NodeInformationDao {

  /**
   * Gets self reported node information from a single node.
   * 
   * @param nodeAddress Address to find the node on.
   * @return Information about the node.
   * @throws NodeConnectionException if there's a problem connecting to the node.
   */
  public NodeInformation getInfo(String nodeAddress) throws NodeConnectionException;

}
