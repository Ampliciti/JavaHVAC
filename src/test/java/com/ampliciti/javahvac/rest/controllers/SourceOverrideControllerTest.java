/*
 * Copyright (C) 2019-2020 jeffrey
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
package com.ampliciti.javahvac.rest.controllers;

import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.dao.domain.OverrideEnum;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.ResponseOptions;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.equalTo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jeffrey
 */
public class SourceOverrideControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(SourceOverrideControllerTest.class);

  public SourceOverrideControllerTest() {}

  @Before
  @Override
  public void setUp() {
    OverrideHolder.clearOverrides();
  }

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testOverrideOn() {
    System.out.println("override on");
    ResponseOptions response = given().body("{\"name\":\"recirculatorPump\",\"state\":\"OVERRIDE_ON\"}").expect()
        .statusCode(201).body("name", equalTo("recirculatorPump")).body("state", equalTo("OVERRIDE_ON")).when()
        .post("/sourceOverride").andReturn();
    assertEquals(OverrideEnum.OVERRIDE_ON, OverrideHolder.getSourceOverride("recirculatorPump"));
  }

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testOverrideOff() {
    System.out.println("override off");
    ResponseOptions response = given().body("{\"name\":\"recirculatorPump\",\"state\":\"OVERRIDE_OFF\"}").expect()
        .statusCode(201).body("name", equalTo("recirculatorPump")).body("state", equalTo("OVERRIDE_OFF")).when()
        .post("/sourceOverride").andReturn();
    assertEquals(OverrideEnum.OVERRIDE_OFF, OverrideHolder.getSourceOverride("recirculatorPump"));
  }

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testOverrideRun() {
    System.out.println("override run");
    ResponseOptions response = given().body("{\"name\":\"recirculatorPump\",\"state\":\"RUN\"}").expect()
        .statusCode(201).body("name", equalTo("recirculatorPump")).body("state", equalTo("RUN")).when()
        .post("/sourceOverride").andReturn();
    assertEquals(OverrideEnum.RUN, OverrideHolder.getSourceOverride("recirculatorPump"));
  }

  /**
   * Test of GET method, of class SourceOverrideController.
   */
  @Test
  public void testGetAllOverridesEmpty() throws Exception {
    System.out.println("getAllOverrides empty");
    ResponseOptions response = given().expect().statusCode(200).when().get("/sourceOverride").andReturn();
    String responseBody = response.getBody().asString();
    logger.info(response);
    JSONParser parser = new JSONParser();
    JSONArray fullResponse = (JSONArray) parser.parse(responseBody);
    assertNotNull(fullResponse);
    assertEquals(0, fullResponse.size());
  }

  /**
   * Test of GET method, of class SourceOverrideController.
   */
  @Test
  public void testGetAllOverridesSet() throws Exception {
    System.out.println("getAllOverrides set");
    OverrideHolder.setSourceOverride("recirculatorPump", OverrideEnum.OVERRIDE_ON);
    ResponseOptions response = given().expect().statusCode(200).when().get("/sourceOverride").andReturn();
    String responseBody = response.getBody().asString();
    logger.info(response);
    JSONParser parser = new JSONParser();
    JSONArray fullResponse = (JSONArray) parser.parse(responseBody);
    assertNotNull(fullResponse);
    assertEquals(1, fullResponse.size());
    JSONObject element1 = (JSONObject) fullResponse.get(0);
    assertNotNull(element1);
    ArrayList<LinkedTreeMap> overridesList =
        (ArrayList<LinkedTreeMap>) new Gson().fromJson(fullResponse.toJSONString(), ArrayList.class);
    assertEquals("recirculatorPump", overridesList.get(0).get("name"));
    assertEquals(OverrideEnum.OVERRIDE_ON.toString(), overridesList.get(0).get("state"));
  }

  /**
   * Test of GET method, of class SourceOverrideController.
   */
  @Test
  public void testGetAllOverridesSetMultiple() throws Exception {
    System.out.println("getAllOverrides set multiple");
    OverrideHolder.setSourceOverride("recirculatorPump", OverrideEnum.OVERRIDE_ON);
    OverrideHolder.setSourceOverride("housePump", OverrideEnum.OVERRIDE_OFF);
    OverrideHolder.setSourceOverride("whatever", OverrideEnum.RUN);
    ResponseOptions response = given().expect().statusCode(200).when().get("/sourceOverride").andReturn();
    String responseBody = response.getBody().asString();
    logger.info(response);
    JSONParser parser = new JSONParser();
    JSONArray fullResponse = (JSONArray) parser.parse(responseBody);
    assertNotNull(fullResponse);
    assertEquals(2, fullResponse.size());
    ArrayList<LinkedTreeMap> overridesList =
        (ArrayList<LinkedTreeMap>) new Gson().fromJson(fullResponse.toJSONString(), ArrayList.class);
    assertEquals("recirculatorPump", overridesList.get(1).get("name"));
    assertEquals(OverrideEnum.OVERRIDE_ON.toString(), overridesList.get(1).get("state"));
    assertEquals("housePump", overridesList.get(0).get("name"));
    assertEquals(OverrideEnum.OVERRIDE_OFF.toString(), overridesList.get(0).get("state"));
  }
}
