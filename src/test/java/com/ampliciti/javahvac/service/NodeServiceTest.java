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
package com.ampliciti.javahvac.service;

import com.ampliciti.javahvac.config.ServerConfig;
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
public class NodeServiceTest {

  public NodeServiceTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of checkNodeConnections method, of class NodeService.
   */
  @Test
  public void testCheckNodeConnectionsNoConnections() {
    System.out.println("checkNodeConnectionsNoConnections");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // test
    NodeService instance = new NodeService();
    boolean expResult = false;
    boolean result = instance.checkNodeConnections();
    assertEquals(expResult, result);
  }

  /**
   * Test of checkNodeConnections method, of class NodeService.
   */
  @Test
  public void testCheckNodeConnectionsWithConnections() throws Exception {
    System.out.println("checkNodeConnectionsWithConnections");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    int testPort = 8082;
    ClientAndServer mockServer = ClientAndServer.startClientAndServer(testPort);
    mockServer.when(request().withPath("/info"))
        .respond(response()
            .withBody(FileUtils.readFileToString(
                new File("./config-samples/node-json/barn-node-info.json"), "UTF-8"))
            .withStatusCode(200));

    // test
    NodeService instance = new NodeService();
    boolean expResult = true;
    boolean result = instance.checkNodeConnections();
    assertEquals(expResult, result);
  }

}
