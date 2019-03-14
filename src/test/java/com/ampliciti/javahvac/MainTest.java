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
package com.ampliciti.javahvac;

import com.ampliciti.javahvac.config.ServerConfig;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

/**
 *
 * @author jeffrey
 */
public class MainTest {

  @Rule
  public final ExpectedSystemExit exit = ExpectedSystemExit.none();

  public MainTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of main method, of class Main.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    exit.expectSystemExit();
    String[] args = null;
    Main.main(args);
  }

  /**
   * Test of main method, of class Main.
   */
  @Test
  public void testMain0() {
    System.out.println("main");
    exit.expectSystemExit();
    String[] args = {"", ""};
    Main.main(args);
  }

  /**
   * Test of main method, of class Main.
   */
  @Test
  public void testMain1() {
    System.out.println("main");
    String[] args = {"./config-samples/server.yaml.sample"};
    Main.main(args);
  }

  /**
   * Test of main method, of class Main.
   */
  @Test
  public void testMain2() {
    System.out.println("main");
    exit.expectSystemExit();
    String[] args = {"./config-samples/server.yaml.sample.missing"};
    Main.main(args);
  }

  /**
   * Test of run method, of class Main.
   */
  @Test
  public void testRun() {
    System.out.println("run");
    File yamlFile = new File("./config-samples/server.yaml.sample");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    Main instance = new Main(yamlFile);
    instance.initApp();
    // at least check that something got loaded into config
    assertEquals("Name of Building Complex", ServerConfig.getName());
  }

}
