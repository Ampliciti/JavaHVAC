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
package com.ampliciti.javahvac;

import java.net.MalformedURLException;
import java.net.URL;
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
public class UtilsTest {

  public UtilsTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of buildUrlFromAddressString method, of class Utils.
   */
  @Test
  public void testBuildUrlFromAddressString() throws MalformedURLException {
    System.out.println("buildUrlFromAddressString");
    String address = "attic.lan";
    URL expResult = new URL("http", "attic.lan", 80, "");
    URL result = Utils.buildUrlFromAddressString(address);
    assertEquals(expResult, result);
    assertEquals("http://attic.lan:80", result.toExternalForm());
  }

  /**
   * Test of buildUrlFromAddressString method, of class Utils.
   */
  @Test
  public void testBuildUrlFromAddressStringNonDefaultPort() throws MalformedURLException {
    System.out.println("buildUrlFromAddressString");
    String address = "attic.lan:8080";
    URL expResult = new URL("http", "attic.lan", 8080, "");
    URL result = Utils.buildUrlFromAddressString(address);
    assertEquals(expResult, result);
    assertEquals("http://attic.lan:8080", result.toExternalForm());
  }

}
