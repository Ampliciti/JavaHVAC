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
public class SourceOverrideControllerTest extends ParentControllerTest {

  private static Logger logger = Logger.getLogger(ZoneControllerTest.class);

  public SourceOverrideControllerTest() {}

  /**
   * Test of create method, of class SourceOverrideController.
   */
  @Test
  public void testCreate() {
    System.out.println("create");
//    super.mockServerAttic
//        .when(request().withPath("/action")
//            .withBody(exact("{\"name\":\"house_floods\",\"state\":true}")))
//        .respond(
//            response().withBody("{\"name\":\"house_floods\",\"state\":true}").withStatusCode(201));
//    VerificationTimes.exactly(1);
//    CurrentNodeState.refreshNodeState();// build our registry of nodes

    ResponseOptions response = given().body("{\"name\":\"recirculatorPump\",\"state\":\"OVERRIDE_ON\"}").expect()
        .statusCode(201).body("name", equalTo("recirculatorPump")).body("state", equalTo("OVERRIDE_ON")).when()
        .post("/sourceOverride").andReturn();
  }

}
