/*
 * Copyright (C) 2021 jeffrey
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
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.dao.domain.OverrideEnum;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.config.Region;
import java.io.File;
import org.junit.Before;
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
public class RegionRuleTest extends ParentNodeTest {

  public RegionRuleTest() {}


  @Before
  public void setUpRegionRule() {
    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    startMocks();
    CurrentNodeState.refreshNodeState();// build our registry of nodes
  }

  /**
   * Test of getDefinition method, of class RegionRule.
   */
  @Test
  public void testGetDefinition() {
    System.out.println("getDefinition");
    Region house = new Region();
    house.setName("house");
    RegionRule instance = new RegionRule(house);
    String expResult = "Rule for Region: house";
    String result = instance.getDefinition();
    assertEquals(expResult, result);
  }

  /**
   * Test of enforceRule method, of class RegionRule.
   */
  @Test
  public void testEnforceRuleNoOverride() {
    System.out.println("enforceRuleNoOverride");
    Region house = new Region();
    house.setName("house");
    RegionRule instance = new RegionRule(house);
    boolean expResult = true;
    boolean result = instance.enforceRule();
    assertEquals(expResult, result);
  }

  /**
   * Test of enforceRule method, of class RegionRule.
   */
  @Test
  public void testEnforceRuleOverrideOff() {
    System.out.println("enforceRuleOverrideOff");
    OverrideHolder.setRegionOverride("house", OverrideEnum.OVERRIDE_OFF);
    Region house = new Region();
    house.setName("house");
    RegionRule instance = new RegionRule(house);

    boolean expResult = true;
    super.mockServerCistern
        .when(request().withPath("/action").withBody(exact("{\"name\":\"housePump\",\"state\":false}")))
        .respond(response().withBody("{\"name\":\"housePump\",\"state\":false}").withStatusCode(201));

    VerificationTimes.exactly(1);

    boolean result = instance.enforceRule();
    assertEquals(expResult, result);
  }

  /**
   * Test of enforceRule method, of class RegionRule.
   */
  @Test
  public void testEnforceRuleOverrideOn() {
    System.out.println("enforceRuleOverrideOn");
    OverrideHolder.setRegionOverride("house", OverrideEnum.OVERRIDE_ON);
    Region house = new Region();
    house.setName("house");
    RegionRule instance = new RegionRule(house);
    boolean expResult = true;
    startMocks();
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    super.mockServerCistern
        .when(request().withPath("/action").withBody(exact("{\"name\":\"housePump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"housePump\",\"state\":true}").withStatusCode(201));

    VerificationTimes.exactly(1);

    boolean result = instance.enforceRule();
    assertEquals(expResult, result);
  }

  // /**
  // * Test of enforceRule method, of class RegionRule.
  // */
  // @Test
  // public void testEnforceRuleOverrideRun() {
  // System.out.println("enforceRuleOverrideRun");
  // OverrideHolder.setRegionOverride("house", OverrideEnum.RUN);
  // Region house = new Region();
  // house.setName("house");
  // RegionRule instance = new RegionRule(house);
  // boolean expResult = true;
  // startMocks();
  // CurrentNodeState.refreshNodeState();// build our registry of nodes
  //
  // super.mockServerCistern
  // .when(request().withPath("/action")
  // .withBody(exact("{\"name\":\"housePump\",\"state\":true}")))
  // .respond(
  // response().withBody("{\"name\":\"housePump\",\"state\":true}").withStatusCode(201));
  //
  // VerificationTimes.exactly(1);
  //
  // boolean result = instance.enforceRule();
  // assertEquals(expResult, result);
  // }

}
