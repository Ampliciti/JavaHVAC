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
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.NodeInformation;
import com.ampliciti.javahvac.domain.NodeMiscInformation;
import com.ampliciti.javahvac.domain.NodeSourceInformation;
import com.ampliciti.javahvac.domain.NodeZoneInformation;
import com.ampliciti.javahvac.domain.config.Node;
import com.ampliciti.javahvac.exceptions.PermissionsException;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import org.mockserver.verify.VerificationTimes;

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
    assertEquals(4, res.size());
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

  /**
   * Test of changeZoneState method, of class NodeService.
   */
  @Test
  public void testChangeZoneState() throws Exception {
    System.out.println("changeZoneState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    super.mockServerAttic
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"house_floods\",\"state\":true}")))
        .respond(
            response().withBody("{\"name\":\"house_floods\",\"state\":true}").withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    String zoneName = "house_floods";
    boolean command = true;
    NodeService instance = new NodeService();
    boolean expResult = true;
    boolean result = instance.changeZoneState(zoneName, command);
    assertEquals(expResult, result);
  }

  /**
   * Test of changeSourceState method, of class NodeService.
   */
  @Test
  public void testChangeSourceState() throws Exception {
    System.out.println("changeSourceState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":true}")
            .withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    String sourceName = "recirculatorPump";
    boolean command = true;
    NodeService instance = new NodeService();
    boolean expResult = true;
    boolean result = instance.changeSourceState(sourceName, command);
    assertEquals(expResult, result);
  }

  /**
   * Test of endUserChangeZoneState method, of class NodeService.
   */
  @Test
  public void testEndUserChangeZoneState() throws Exception {
    System.out.println("endUserChangeZoneState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    super.mockServerAttic
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"house_floods\",\"state\":true}")))
        .respond(
            response().withBody("{\"name\":\"house_floods\",\"state\":true}").withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    String zoneName = "house_floods";
    boolean command = true;
    NodeService instance = new NodeService();
    boolean expResult = true;
    boolean result = instance.endUserChangeZoneState(zoneName, command);
    assertEquals(expResult, result);
  }

  /**
   * Test of endUserChangeZoneState method, of class NodeService.
   */
  @Test(expected = PermissionsException.class)
  public void testEndUserChangeZoneStatePermissionDenied() throws Exception {
    System.out.println("endUserChangeZoneState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    super.mockServerCentral
        .when(request().withPath("/action").withBody(exact("{\"zone\":\"hall\",\"state\":true}")))
        .respond(response().withBody("{\"zone\":\"hall\",\"state\":true}").withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    String zoneName = "hall";
    boolean command = true;
    NodeService instance = new NodeService();
    instance.endUserChangeZoneState(zoneName, command); // should throw an exception
  }

  @Test
  public void testLookUpNodesForSource() throws Exception {
    System.out.println("testLookUpNodesForSource");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    CurrentNodeState.refreshNodeState();// build our registry of nodes

    String sourceName = "cistern";
    NodeService instance = new NodeService();
    ArrayList<Node> cisternNodes = instance.lookUpNodesForSource(sourceName);
    assertEquals(1, cisternNodes.size());
  }

  @Test
  public void testLookUpNodeForRegionControl() throws Exception {
    System.out.println("testLookUpNodeForRegionControl");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    CurrentNodeState.refreshNodeState();// build our registry of nodes

    String regionControlName = "house";
    NodeService instance = new NodeService();
    Node houseRegionControlNode = instance.lookUpNodeForRegionControl(regionControlName);
    assertNotNull(houseRegionControlNode);
    assertEquals("pump-pi", houseRegionControlNode.getName());
  }

  @Test
  public void testLookupAllZones() {
    System.out.println("testLookupAllZones");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    CurrentNodeState.refreshNodeState();// build our registry of nodes
    // test
    NodeService instance = new NodeService();
    ArrayList<NodeZoneInformation> allZones = instance.lookupAllZones();
    assertNotNull(allZones);
    assertEquals(8, allZones.size());
  }

  @Test
  public void testLookupAllSources() {
    System.out.println("testLookupAllSources");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    CurrentNodeState.refreshNodeState();// build our registry of nodes
    // test
    NodeService instance = new NodeService();
    ArrayList<NodeSourceInformation> allSources = instance.lookupAllSources();
    assertNotNull(allSources);
    assertEquals(8, allSources.size());
  }


  @Test
  public void testLookupAllMisc() {
    System.out.println("testLookupAllSources");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();
    CurrentNodeState.refreshNodeState();// build our registry of nodes
    // test
    NodeService instance = new NodeService();
    ArrayList<NodeMiscInformation> allMisc = instance.lookupAllMisc();
    assertNotNull(allMisc);
    assertEquals(1, allMisc.size());
  }
}
