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

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.domain.NodeInformation;
import com.ampliciti.javahvac.domain.config.Node;
import java.io.File;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffrey
 */
public class NodeServiceTest extends ParentNodeTest {

  public NodeServiceTest() {}


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
    startMocks();

    // test
    NodeService instance = new NodeService();
    boolean expResult = true;
    boolean result = instance.checkNodeConnections();
    assertEquals(expResult, result);
  }

  /**
   * Test of checkNodeConnections method, of class NodeService.
   */
  @Test
  public void testPullNodeState() throws Exception {
    System.out.println("testPullNodeState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    // test
    NodeService instance = new NodeService();
    Map<String, NodeInformation> res = instance.pullNodeState();
    assertNotNull(res);
    assertEquals(3, res.size());
    assertEquals("hall", res.get("house-central").getZones().get(0).getName());
  }

  /**
   * Test of checkNodeConnections method, of class NodeService.
   */
  @Test
  public void testPullNodeStateMissingNodes() throws Exception {
    System.out.println("testPullNodeStateMissingNodes");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    // test
    NodeService instance = new NodeService();
    Map<String, NodeInformation> res = instance.pullNodeState();
    assertNotNull(res);
  }

  /**
   * Test of checkNodeConnections method, of class NodeService.
   */
  @Test
  public void testPullNodeStateSingle() throws Exception {
    System.out.println("testPullNodeState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    // test
    NodeService instance = new NodeService();
    NodeInformation result = instance.pullNodeState(new Node("house-attic", "localhost:8084"));
    assertNotNull(result);
    assertEquals(1, result.getZones().size());
  }

}
