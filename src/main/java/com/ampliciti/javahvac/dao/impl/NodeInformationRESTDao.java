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
import com.ampliciti.javahvac.domain.NodeInformation;
import java.net.URL;

/**
 * REST Dao for getting self-reported information from nodes.
 * 
 * @author jeffrey
 */
public class NodeInformationRESTDao implements NodeInformationDao {

  public NodeInformationRESTDao() {}

  /**
   * Gets self reported node information from a single node.
   * 
   * @param nodeAddress Address to find the node on.
   * @return
   */
  @Override
  public NodeInformation getInfo(String nodeAddress) {
    RESTDao restDao = new RESTDaoImpl(Utils.buildUrlFromAddressString(nodeAddress));
    // restDao.doGetCall(nodeAddress);
    throw new UnsupportedOperationException("Not done yet");
  }

}
