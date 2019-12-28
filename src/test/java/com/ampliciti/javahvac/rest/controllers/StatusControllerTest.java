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

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.ResponseOptions;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jeffrey
 */
public class StatusControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(StatusControllerTest.class);

  public StatusControllerTest() {}


  @Test
  public void testGetStatus() {
    System.out.println("getStatus");
    logger.info("get status test start");
    ResponseOptions response = given().expect().statusCode(200).when().get("/status").andReturn();
    ArrayList<LinkedHashMap> items = response.getBody().as(ArrayList.class);
    Assert.assertNotNull(items);
    Assert.assertEquals(4, items.size());
    Assert.assertEquals("barn", items.get(0).get("name"));
    Assert.assertNotNull(items.get(0).get("zones"));
    Assert.assertEquals("house-attic", items.get(1).get("name"));
    Assert.assertNotNull(items.get(1).get("zones"));
    Assert.assertEquals("house-central", items.get(2).get("name"));
    Assert.assertNotNull(items.get(2).get("zones"));
    logger.info("get status test end");
  }

}
