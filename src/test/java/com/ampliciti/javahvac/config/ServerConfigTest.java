/*
 * Copyright (C) 2018 jeffrey
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
package com.ampliciti.javahvac.config;

import com.ampliciti.javahvac.domain.Source.Type;
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
public class ServerConfigTest {

  public ServerConfigTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of buildConfig method, of class Config.
   */
  @Test
  public void testBuildConfig() throws Exception {
    System.out.println("buildConfig");
    File yamlFile = new File("./config-samples/server.yaml.sample");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    assertEquals("Name of Building Complex", ServerConfig.getName());
    assertEquals("127.0.0.1", ServerConfig.getDns());
    assertEquals(3, ServerConfig.getRegions().size());
    assertEquals("house", ServerConfig.getRegions().get(0).getName());
    assertEquals("hall", ServerConfig.getRegions().get(0).getZones().get(0).getName());
    assertEquals("shop", ServerConfig.getRegions().get(1).getName());
    assertEquals("lights", ServerConfig.getRegions().get(2).getName());

    assertEquals(0, ServerConfig.getRegions().get(1).getZones().get(0).getRuntime());
    assertEquals(false, ServerConfig.getRegions().get(1).getZones().get(0).isManualAllowed());
    assertEquals("mill", ServerConfig.getRegions().get(1).getZones().get(0).getName());

    assertEquals("chicken_coop", ServerConfig.getRegions().get(1).getZones().get(1).getName());
    assertEquals(5, ServerConfig.getRegions().get(1).getZones().get(1).getRuntime());
    assertEquals(true, ServerConfig.getRegions().get(1).getZones().get(1).isManualAllowed());

    assertEquals("house_floods", ServerConfig.getRegions().get(2).getZones().get(0).getName());
    assertEquals(0, ServerConfig.getRegions().get(2).getZones().get(0).getRuntime());
    assertEquals(true, ServerConfig.getRegions().get(2).getZones().get(0).isManualAllowed());

    assertEquals("barn.lan", ServerConfig.getNodes().get(0).getAddress());
    assertEquals("central.lan", ServerConfig.getNodes().get(1).getAddress());

    assertEquals("furnace", ServerConfig.getSources().get(0).getName());
    assertEquals("house", ServerConfig.getSources().get(0).getRegions_served().get(0));
    assertEquals(Type.HEAT, ServerConfig.getSources().get(0).getType());
    assertEquals("ac", ServerConfig.getSources().get(1).getName());
    assertEquals("cistern", ServerConfig.getSources().get(2).getName());

  }

}
