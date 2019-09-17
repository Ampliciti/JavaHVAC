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

import com.ampliciti.javahvac.domain.NodeInformation;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 *
 * @author jeffrey
 */
public class NodeInformationRESTDaoTest {

  public NodeInformationRESTDaoTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getInfo method, of class NodeInformationRESTDao.
   */
  @Test
  public void testGetInfo() throws Exception {
    System.out.println("getInfo");
    // setup the mock
    int testPort = 8081;
    ClientAndServer mockServer = ClientAndServer.startClientAndServer(testPort);
    mockServer.when(request().withPath("/info"))
        .respond(response()
            .withBody(FileUtils.readFileToString(
                new File("./config-samples/node-json/barn-node-info.json"), "UTF-8"))
            .withStatusCode(200));
    String nodeAddress = "localhost:" + testPort;

    NodeInformationRESTDao instance = new NodeInformationRESTDao();
    NodeInformation result = instance.getInfo(nodeAddress);
    assertNotNull(result);
    assertEquals("barn", result.getName());
    assertEquals("barn.lan", result.getAddress());
    assertEquals(5, result.getZones().size());
    assertEquals("mill", result.getZones().get(0).getName());
    assertEquals(new Double(75.4), result.getZones().get(0).getTemp());
    assertEquals(false, result.getZones().get(0).getState());
    // ...
    assertEquals("equipment_breaker", result.getZones().get(2).getName());
    assertEquals(true, result.getZones().get(2).getState());
  }

}
