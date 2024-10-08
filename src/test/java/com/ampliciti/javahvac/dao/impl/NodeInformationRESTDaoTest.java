/*
 * Copyright (C) 2019 jeffrey
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.domain.NodeInformation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffrey
 */
public class NodeInformationRESTDaoTest extends ParentNodeTest {

  public NodeInformationRESTDaoTest() {}

  /**
   * Test of getInfo method, of class NodeInformationRESTDao.
   */
  @Test
  public void testGetInfo() throws Exception {
    System.out.println("getInfo");
    // setup the mock
    startMocks();
    int testPort = 8082;
    String nodeAddress = "localhost:" + testPort;

    NodeInformationRESTDao instance = new NodeInformationRESTDao();
    NodeInformation result = instance.getInfo(nodeAddress);
    assertNotNull(result);
    assertEquals("barn", result.getName());
    assertEquals("barn.lan", result.getAddress());
    assertEquals(6, result.getZones().size());
    assertEquals("mill", result.getZones().get(0).getName());
    assertEquals(new Double(75.4), result.getZones().get(0).getTemp());
    assertEquals(false, result.getZones().get(0).getState());
    // ...
    assertEquals("equipment_breaker", result.getZones().get(2).getName());
    assertEquals(true, result.getZones().get(2).getState());
  }

  /**
   * Test of getInfo method, of class NodeInformationRESTDao.
   */
  @Test
  public void testGetInfoWithSourceAndMisc() throws Exception {
    System.out.println("getInfoWithSourceAndMisc");
    // setup the mock
    startMocks();
    int testPort = 8085;
    String nodeAddress = "localhost:" + testPort;

    NodeInformationRESTDao instance = new NodeInformationRESTDao();
    NodeInformation result = instance.getInfo(nodeAddress);
    assertNotNull(result);
    assertEquals("pump-pi", result.getName());
    assertEquals("pump-pi.lan", result.getAddress());
    assertEquals(0, result.getZones().size());
    assertEquals(8, result.getSources().size());
    assertEquals("cisternBottom", result.getSources().get(0).getName());
    assertEquals(new Double(65.975), result.getSources().get(0).getTemp());
    // ...
    assertEquals("recirculatorPump", result.getSources().get(4).getName());
    assertEquals(null, result.getSources().get(4).getTemp());// no temp here
    assertEquals(false, result.getSources().get(4).getState());

    // ...
    assertEquals("EastTemp", result.getMisc().get(0).getName());
    assertEquals(null, result.getMisc().get(0).getTemp());// no temp here for now
  }

}
