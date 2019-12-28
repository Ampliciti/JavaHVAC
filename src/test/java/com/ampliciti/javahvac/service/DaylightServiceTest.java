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
import com.ampliciti.javahvac.dao.domain.DayLight;
import java.io.File;
import java.util.Date;
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
public class DaylightServiceTest {

  public DaylightServiceTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getDayLight method, of class DaylightService.
   */
  @Test
  public void testGetDayLight() {
    System.out.println("getDayLight");

    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);

    // confirm we don't have an last updated time
    long resultLastUpdated = DaylightService.getLastUpdated();
    assertEquals(0L, resultLastUpdated);
    // get the daylight
    DayLight result = DaylightService.getDayLight();
    assertNotNull(result);
    System.out.println(result.toString());// print for the heck of it
    // check that the last updated time was set
    resultLastUpdated = DaylightService.getLastUpdated();
    long lastUpdatedDelay = new Date().getTime() - resultLastUpdated;
    assertTrue(lastUpdatedDelay < 100);// make sure we were updated within the past 100 ms
    assertTrue(lastUpdatedDelay > 0);// and that we're somehow not in the past
    // call again
    DayLight result1 = DaylightService.getDayLight();
    assertNotNull(result1);
    assertTrue(result == result1);// make sure it's the exact same object that's come back
    assertEquals(resultLastUpdated, DaylightService.getLastUpdated());// and that we haven't changed
                                                                      // the last updated date
  }


}
