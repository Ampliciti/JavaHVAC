/*
 * Copyright (C) 2020 jeffrey
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

import com.ampliciti.javahvac.dao.domain.SourceOverride;
import com.ampliciti.javahvac.domain.OverrideState;
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
public class OverrideHolderTest {

  public OverrideHolderTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getSourceOverride method, of class OverrideHolder.
   */
  @Test
  public void testGetSourceOverrideNotSet() {
    System.out.println("getSourceOverrideNotSet");
    String source = "any arbitrary value";
    SourceOverride expResult = SourceOverride.RUN;
    SourceOverride result = OverrideHolder.getSourceOverride(source);
    assertEquals(expResult, result);
  }

  @Test
  public void testSetGetSourceOverride() {
    System.out.println("setSourceOverride");
    String source = "testSource";
    SourceOverride override = SourceOverride.OVERRIDE_OFF;
    OverrideHolder.setSourceOverride(source, override);
    SourceOverride result = OverrideHolder.getSourceOverride(source);
    assertEquals(override, result);
    ArrayList<OverrideState> allOverrides =
        (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(SourceOverride.OVERRIDE_OFF, allOverrides.get(0).getState());
    override = SourceOverride.OVERRIDE_ON;
    OverrideHolder.setSourceOverride(source, override);
    result = OverrideHolder.getSourceOverride(source);
    assertEquals(override, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(SourceOverride.OVERRIDE_ON, allOverrides.get(0).getState());
    override = SourceOverride.OVERRIDE_OFF;
    OverrideHolder.setSourceOverride(source, override);
    result = OverrideHolder.getSourceOverride(source);
    assertEquals(override, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(SourceOverride.OVERRIDE_OFF, allOverrides.get(0).getState());
    String source1 = "testSource1";
    result = OverrideHolder.getSourceOverride(source1);
    assertEquals(SourceOverride.RUN, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());// we don't don't return overrides in the RUN state from
                                         // the getAll method
    OverrideHolder.setSourceOverride(source1, SourceOverride.OVERRIDE_ON);
    // if this fails, there's an ordering issue and you need to fix this test to search the whole
    // arraylist
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(2, allOverrides.size());
    assertEquals(source, allOverrides.get(1).getName());
    assertEquals(SourceOverride.OVERRIDE_OFF, allOverrides.get(1).getState());
    assertEquals(source1, allOverrides.get(0).getName());
    assertEquals(SourceOverride.OVERRIDE_ON, allOverrides.get(0).getState());
    OverrideHolder.clearOverrides();
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(0, allOverrides.size());
    result = OverrideHolder.getSourceOverride(source);
    assertEquals(SourceOverride.RUN, result);
  }

}
