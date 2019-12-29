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
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffrey
 */
public class SolarCisternRuleTest extends ParentNodeTest {

  public SolarCisternRuleTest() {}


  /**
   * Test of getDefinition method, of class SolarCisternRule.
   */
  @Test
  public void testGetDefinition() {
    System.out.println("getDefinition");
    SolarCisternRule instance = new SolarCisternRule("Cistern", 140);
    String expResult = "Solar cistern rule for: Cistern";
    String result = instance.getDefinition();
    assertEquals(expResult, result);
  }

  /**
   * Test of enforceRule method, of class SolarCisternRule.
   */
  @Test
  public void testEnforceRule() {
    System.out.println("enforceRule");

    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    CurrentNodeState.refreshNodeState();// build our registry of nodes

    // end setup

    SolarCisternRule instance = new SolarCisternRule("Cistern", 120);
    boolean expResult = true;
    boolean result = instance.enforceRule();
    assertEquals(expResult, result);
    assertEquals(65.975, instance.getCisternBottomTemp(), .0001);
    assertEquals(64.85, instance.getCisternInletTemp(), .0001);
    assertEquals(65.975, instance.getCisternTopTemp(), .0001);



  }

}
