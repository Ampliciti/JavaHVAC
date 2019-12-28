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
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.dao.domain.DayLight;
import com.ampliciti.javahvac.domain.config.Location;
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
public class DayLightDaoTest {

  public DayLightDaoTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getDayLight method, of class DayLightDao.
   */
  @Test
  public void testGetDayLight() throws Exception {
    System.out.println("getDayLight");
    Location location = new Location(39.0000, -104.0000);
    DayLightDao instance = new DayLightDao();
    DayLight result = instance.getDayLight(location);
    assertNotNull(result);
    System.out.println(result.toString());
    assertNotNull(result.getSunrise());
    assertNotNull(result.getSunset());
    assertTrue("Sunrise is not after sunset", result.getSunset() > result.getSunrise());
    boolean isLight = result.isDaylight();
    System.out.println("Is it currently light outside? " + isLight);
  }

}
