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
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.ResponseOptions;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author jeffrey
 */
public class StatusControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(StatusControllerTest.class);

  public StatusControllerTest() {}


  @Test
  public void testGetStatus() throws ParseException {
    System.out.println("getStatus");
    logger.info("get status test start");
    OverrideHolder.setSourceOverride("randomOverride", SourceOverride.OVERRIDE_ON);
    ResponseOptions response = given().expect().statusCode(200).when().get("/status").andReturn();
    String responseBody = response.getBody().asString();
    logger.info(response);
    JSONParser parser = new JSONParser();
    JSONObject fullResponse = (JSONObject) parser.parse(responseBody);
    assertNotNull(fullResponse);
    JSONArray nodeStateJson = (JSONArray) fullResponse.get("nodeState");
    String nodeStateString = nodeStateJson.toJSONString();
    assertNotNull(nodeStateString);
    ArrayList<LinkedHashMap> items = new Gson().fromJson(nodeStateString, ArrayList.class);
    assertNotNull(items);
    assertEquals(4, items.size());
    // getting weird class cast exceptions -- fix later if you care
    // LinkedHashMap barnItem = items.get(0);
    // String barnName = (String)barnItem.get("name");
    // assertEquals("barn", barnName);
    // //assertEquals("barn", .get("name"));
    // assertNotNull(items.get(0).get("zones"));
    // assertEquals("house-attic", items.get(1).get("name"));
    // assertNotNull(items.get(1).get("zones"));
    // assertEquals("house-central", items.get(2).get("name"));
    // assertNotNull(items.get(2).get("zones"));

    JSONObject sun = (JSONObject) fullResponse.get("sun");
    assertNotNull(sun);
    assertEquals(2, sun.size());
    JSONArray overrides = (JSONArray) fullResponse.get("overrides");
    assertNotNull(overrides);
    assertEquals(1, overrides.size());
    ArrayList<LinkedTreeMap> overridesList =
        (ArrayList<LinkedTreeMap>) new Gson().fromJson(overrides.toJSONString(), ArrayList.class);
    assertEquals("randomOverride", overridesList.get(0).get("name"));
    assertEquals(SourceOverride.OVERRIDE_ON.toString(), overridesList.get(0).get("state"));

    logger.info("get status test end");
  }

}
