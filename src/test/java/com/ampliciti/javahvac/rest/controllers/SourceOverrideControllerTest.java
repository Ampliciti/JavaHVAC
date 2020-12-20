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
package com.ampliciti.javahvac.rest.controllers;

import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.dao.domain.SourceOverride;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.ResponseOptions;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author jeffrey
 */
public class SourceOverrideControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(SourceOverrideControllerTest.class);

  public SourceOverrideControllerTest() {}

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testOverrideOn() {
    System.out.println("override on");
    OverrideHolder.clearOverrides();
    ResponseOptions response =
        given().body("{\"name\":\"recirculatorPump\",\"state\":\"OVERRIDE_ON\"}").expect()
            .statusCode(201).body("name", equalTo("recirculatorPump"))
            .body("state", equalTo("OVERRIDE_ON")).when().post("/sourceOverride").andReturn();
    assertEquals(SourceOverride.OVERRIDE_ON, OverrideHolder.getSourceOverride("recirculatorPump"));
  }

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testOverrideOff() {
    System.out.println("override off");
    OverrideHolder.clearOverrides();
    ResponseOptions response =
        given().body("{\"name\":\"recirculatorPump\",\"state\":\"OVERRIDE_OFF\"}").expect()
            .statusCode(201).body("name", equalTo("recirculatorPump"))
            .body("state", equalTo("OVERRIDE_OFF")).when().post("/sourceOverride").andReturn();
    assertEquals(SourceOverride.OVERRIDE_OFF, OverrideHolder.getSourceOverride("recirculatorPump"));
  }

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testOverrideRun() {
    System.out.println("override run");
    OverrideHolder.clearOverrides();
    ResponseOptions response = given().body("{\"name\":\"recirculatorPump\",\"state\":\"RUN\"}")
        .expect().statusCode(201).body("name", equalTo("recirculatorPump"))
        .body("state", equalTo("RUN")).when().post("/sourceOverride").andReturn();
    assertEquals(SourceOverride.RUN, OverrideHolder.getSourceOverride("recirculatorPump"));
  }
}
