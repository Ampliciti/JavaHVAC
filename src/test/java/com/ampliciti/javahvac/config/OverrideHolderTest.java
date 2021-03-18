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

import com.ampliciti.javahvac.dao.domain.OverrideEnum;
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
  public void setUp() {
    OverrideHolder.clearOverrides();
  }

  @After
  public void tearDown() {}

  /**
   * Test of getRegionOverride method, of class OverrideHolder.
   */
  @Test
  public void testGetRegionOverrideNotSet() {
    System.out.println("getRegionOverrideNotSet");
    String source = "any arbitrary value";
    OverrideEnum expResult = OverrideEnum.RUN;
    OverrideEnum result = OverrideHolder.getRegionOverride(source);
    assertEquals(expResult, result);
  }

  @Test
  public void testSetGetRegionOverride() {
    System.out.println("setRegionOverride");
    String region = "testRegion";
    OverrideEnum override = OverrideEnum.OVERRIDE_OFF;
    OverrideHolder.setRegionOverride(region, override);
    OverrideEnum result = OverrideHolder.getRegionOverride(region);
    assertEquals(override, result);
    ArrayList<OverrideState> allOverrides =
        (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(region, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(0).getState());
    override = OverrideEnum.OVERRIDE_ON;
    OverrideHolder.setRegionOverride(region, override);
    result = OverrideHolder.getRegionOverride(region);
    assertEquals(override, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(region, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_ON, allOverrides.get(0).getState());
    override = OverrideEnum.OVERRIDE_OFF;
    OverrideHolder.setRegionOverride(region, override);
    result = OverrideHolder.getRegionOverride(region);
    assertEquals(override, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(region, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(0).getState());
    String source1 = "testSource1";
    result = OverrideHolder.getRegionOverride(source1);
    assertEquals(OverrideEnum.RUN, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());// we don't don't return overrides in the RUN state from
                                         // the getAll method
    OverrideHolder.setRegionOverride(source1, OverrideEnum.OVERRIDE_ON);
    // if this fails, there's an ordering issue and you need to fix this test to search the whole
    // arraylist
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(2, allOverrides.size());
    assertEquals(region, allOverrides.get(1).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(1).getState());
    assertEquals(source1, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_ON, allOverrides.get(0).getState());
    OverrideHolder.clearOverrides();
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(0, allOverrides.size());
    result = OverrideHolder.getRegionOverride(region);
    assertEquals(OverrideEnum.RUN, result);
  }

  /**
   * Test of getSourceOverride method, of class OverrideHolder.
   */
  @Test
  public void testGetSourceOverrideNotSet() {
    System.out.println("getSourceOverrideNotSet");
    String source = "any arbitrary value";
    OverrideEnum expResult = OverrideEnum.RUN;
    OverrideEnum result = OverrideHolder.getSourceOverride(source);
    assertEquals(expResult, result);
  }

  @Test
  public void testSetGetSourceOverride() {
    System.out.println("setSourceOverride");
    String source = "testSource";
    OverrideEnum override = OverrideEnum.OVERRIDE_OFF;
    OverrideHolder.setSourceOverride(source, override);
    OverrideEnum result = OverrideHolder.getSourceOverride(source);
    assertEquals(override, result);
    ArrayList<OverrideState> allOverrides =
        (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(0).getState());
    override = OverrideEnum.OVERRIDE_ON;
    OverrideHolder.setSourceOverride(source, override);
    result = OverrideHolder.getSourceOverride(source);
    assertEquals(override, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_ON, allOverrides.get(0).getState());
    override = OverrideEnum.OVERRIDE_OFF;
    OverrideHolder.setSourceOverride(source, override);
    result = OverrideHolder.getSourceOverride(source);
    assertEquals(override, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(0).getState());
    String source1 = "testSource1";
    result = OverrideHolder.getSourceOverride(source1);
    assertEquals(OverrideEnum.RUN, result);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(1, allOverrides.size());// we don't don't return overrides in the RUN state from
                                         // the getAll method
    OverrideHolder.setSourceOverride(source1, OverrideEnum.OVERRIDE_ON);
    // if this fails, there's an ordering issue and you need to fix this test to search the whole
    // arraylist
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(2, allOverrides.size());
    assertEquals(source, allOverrides.get(1).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(1).getState());
    assertEquals(source1, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_ON, allOverrides.get(0).getState());
    OverrideHolder.clearOverrides();
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(0, allOverrides.size());
    result = OverrideHolder.getSourceOverride(source);
    assertEquals(OverrideEnum.RUN, result);
  }

  @Test
  public void testGetAllOverrides() {
    ArrayList<OverrideState> allOverrides =
        (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(0, allOverrides.size());
    // put two run overrides in:
    // one source
    OverrideHolder.setSourceOverride("randomRegionOverride", OverrideEnum.RUN);
    // one region
    OverrideHolder.setRegionOverride("randomRegionOverride", OverrideEnum.RUN);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(0, allOverrides.size());// run overrides should never return from this method

    String source = "testSource";
    String region = "testRegion";
    OverrideHolder.setSourceOverride(source, OverrideEnum.OVERRIDE_ON);
    OverrideHolder.setRegionOverride(region, OverrideEnum.OVERRIDE_OFF);
    allOverrides = (ArrayList<OverrideState>) OverrideHolder.getAllOverrides();
    assertEquals(2, allOverrides.size());// should have two now
    assertEquals(source, allOverrides.get(0).getName());
    assertEquals(OverrideEnum.OVERRIDE_ON, allOverrides.get(0).getState());
    assertEquals(region, allOverrides.get(1).getName());
    assertEquals(OverrideEnum.OVERRIDE_OFF, allOverrides.get(1).getState());

  }

}
