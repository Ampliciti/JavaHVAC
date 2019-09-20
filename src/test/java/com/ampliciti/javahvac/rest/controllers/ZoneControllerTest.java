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

import com.ampliciti.javahvac.domain.CurrentNodeState;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.response.ResponseOptions;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import org.mockserver.verify.VerificationTimes;

/**
 *
 * @author jeffrey
 */
public class ZoneControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(ZoneControllerTest.class);

  public ZoneControllerTest() {}

  /**
   * Test of create method, of class ZoneController.
   */
  @Test
  public void testCreate() {
    System.out.println("create");
    super.mockServerAttic
        .when(request().withPath("/action")
            .withBody(exact("{\"zone\":\"house_floods\",\"state\":true}")))
        .respond(
            response().withBody("{\"zone\":\"house_floods\",\"state\":true}").withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    ResponseOptions response = given().body("{\"zone\":\"house_floods\",\"state\":true}").expect()
        .statusCode(201).body("zone", equalTo("house_floods")).body("state", equalTo(true)).when()
        .post("/zone").andReturn();
  }

  /**
   * Test of update method, of class ZoneController.
   */
  @Test
  public void testUpdate() {
    System.out.println("update");
    super.mockServerAttic
        .when(request().withPath("/action")
            .withBody(exact("{\"zone\":\"house_floods\",\"state\":true}")))
        .respond(
            response().withBody("{\"zone\":\"house_floods\",\"state\":true}").withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    ResponseOptions response = given().body("{\"zone\":\"house_floods\",\"state\":true}").expect()
        .statusCode(201).body("zone", equalTo("house_floods")).body("state", equalTo(true)).when()
        .put("/zone").andReturn();
  }

  /**
   * Test of create method, of class ZoneController.
   */
  @Test
  public void testCreateFail() {
    System.out.println("create");
    super.mockServerCentral
        .when(request().withPath("/action").withBody(exact("{\"zone\":\"hall\",\"state\":true}")))
        .respond(response().withBody("{\"zone\":\"hall\",\"state\":true}").withStatusCode(201));
    VerificationTimes.exactly(1);
    CurrentNodeState.refreshNodeState();// build our registry of nodes

    ResponseOptions response = given().body("{\"zone\":\"hall\",\"state\":true}").expect()
        .statusCode(403).when().post("/zone").andReturn();
    assertTrue(
        response.getBody().print().contains("This zone is not allowed to be manually changed."));
  }

}
