/*
 * Copyright (C) 2019-2022 jeffrey
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
import com.ampliciti.javahvac.dao.domain.OverrideEnum;
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
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jeffrey
 */
public class StatusCleanControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(StatusCleanControllerTest.class);

  public StatusCleanControllerTest() {}

  @Before
  @Override
  public void setUp() {
    OverrideHolder.clearOverrides();
  }



  @Test
  public void testGetStatus() throws ParseException {
    System.out.println("getStatusClean");
    logger.info("get status test start");
    OverrideHolder.setSourceOverride("randomOverride", OverrideEnum.OVERRIDE_ON);
    ResponseOptions response =
        given().expect().statusCode(200).when().get("/statusClean").andReturn();
    String responseBody = response.getBody().asString();
    logger.info(response);
    JSONParser parser = new JSONParser();
    JSONObject fullResponse = (JSONObject) parser.parse(responseBody);
    assertNotNull(fullResponse);
    JSONArray zonesJson = (JSONArray) fullResponse.get("zones");
    assertNotNull(zonesJson);
    ArrayList<LinkedHashMap> zones = new Gson().fromJson(zonesJson.toJSONString(), ArrayList.class);
    assertNotNull(zones);
    assertEquals(9, zones.size());

    JSONArray sourcesJson = (JSONArray) fullResponse.get("sources");
    assertNotNull(sourcesJson);
    ArrayList<LinkedHashMap> sources =
        new Gson().fromJson(sourcesJson.toJSONString(), ArrayList.class);
    assertNotNull(sources);
    assertEquals(8, sources.size());

    JSONArray miscJson = (JSONArray) fullResponse.get("misc");
    assertNotNull(miscJson);
    ArrayList<LinkedHashMap> misc = new Gson().fromJson(miscJson.toJSONString(), ArrayList.class);
    assertNotNull(misc);
    assertEquals(1, misc.size());

    JSONObject sun = (JSONObject) fullResponse.get("sun");
    assertNotNull(sun);
    assertEquals(2, sun.size());
    JSONArray overrides = (JSONArray) fullResponse.get("overrides");
    assertNotNull(overrides);
    assertEquals(1, overrides.size());
    ArrayList<LinkedTreeMap> overridesList =
        (ArrayList<LinkedTreeMap>) new Gson().fromJson(overrides.toJSONString(), ArrayList.class);
    assertEquals("randomOverride", overridesList.get(0).get("name"));
    assertEquals(OverrideEnum.OVERRIDE_ON.toString(), overridesList.get(0).get("state"));

    logger.info("get status test end");
  }

}
