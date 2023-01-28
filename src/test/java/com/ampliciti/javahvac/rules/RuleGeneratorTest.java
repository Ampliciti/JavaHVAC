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
package com.ampliciti.javahvac.rules;

import com.ampliciti.javahvac.config.ServerConfig;
import java.io.File;
import java.util.ArrayList;
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
public class RuleGeneratorTest {

  public RuleGeneratorTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of generateRules method, of class RuleGenerator.
   */
  @Test
  public void testGenerateManagedRules() {
    System.out.println("generateManagedRules");

    File yamlFile = new File("./config-samples/server.yaml.sample");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);

    ArrayList<Rule> result = RuleGenerator.generateManagedRules();
    assertEquals(6, result.size());
    assertEquals("Solar cistern rule for: cistern", result.get(0).getDefinition());
    assertEquals("Rule for heat source: furnace for regions: house", result.get(1).getDefinition());
    assertEquals("Rule for heat source: ac for regions: house", result.get(2).getDefinition());
    assertEquals("Rule for heat source: cistern for regions: house shop", result.get(3).getDefinition());
    assertEquals("Rule for Region: house", result.get(4).getDefinition());
    assertEquals("Rule for Region: shop", result.get(5).getDefinition());

  }

  @Test
  public void testGenerageNonManagedRules() {
    System.out.println("testGenerageNonManagedRules");
    File yamlFile = new File("./config-samples/server.yaml.sample");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    ArrayList<Rule> result = RuleGenerator.generateNonManagedZoneRules();
    assertEquals("Rule for zone: house_floods", result.get(0).getDefinition());
    assertEquals("Rule for zone: barn_floods", result.get(1).getDefinition());
  }

}
