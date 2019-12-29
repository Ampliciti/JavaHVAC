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
package com.ampliciti.javahvac.dao.domain;

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
public class DayLightTest {

  public DayLightTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getSunrise method, of class DayLight.
   */
  @Test
  public void testGetSunriseAndSunset() {
    System.out.println("getSunriseAndSunset");
    DayLight instance = new DayLight(1577538000000l, 1577581200000l);// not real times, but close
                                                                     // enough
    long result = instance.getSunrise();
    assertEquals(1577538000000l, result);
    result = instance.getSunset();
    assertEquals(1577581200000l, result);
  }

  /**
   * Test of isDaylight method, of class DayLight.
   */
  @Test
  public void testIsDaylight() {
    System.out.println("isDaylight");
    DayLight instance = new DayLight(1577538000000l, 1577581200000l);// not real times, but close
                                                                     // enough
    boolean result = instance.isDaylight();
    // just make sure the above doesn't error -- since this relys on actual clock time, the test
    // would fail or pass depending on the time of day it was run
    // assertEquals(expResult, result);
  }

}
