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
package com.ampliciti.javahvac.domain;

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.domain.config.Node;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffrey
 */
public class CurrentNodeStateTest extends ParentNodeTest {

  public CurrentNodeStateTest() {}


  /**
   * Test of refreshSingleNode method, of class CurrentNodeState.
   */
  @Test
  public void testRefreshSingleNode() {
    System.out.println("refreshSingleNode");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    startMocks();
    Node toRefresh = new Node("barn", "localhost:8082");
    CurrentNodeState.refreshSingleNode(toRefresh);
    assertTrue(CurrentNodeState.getCurrentNodeState().containsKey("barn"));
  }

  /**
   * Test of refreshNodeState method, of class CurrentNodeState.
   */
  @Test
  public void testRefreshNodeState() {
    System.out.println("refreshNodeState");
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    startMocks();
    CurrentNodeState.refreshNodeState();
    assertEquals(3, CurrentNodeState.getCurrentNodeState().size());
  }

}
