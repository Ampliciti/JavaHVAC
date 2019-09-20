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
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;

/**
 *
 * @author jeffrey
 */
public class HealthCheckControllerTest extends ParentControllerTest {

  public HealthCheckControllerTest() {}

  /**
   * Test of getHealth method, of class HealthCheckController.
   */
  @Test
  public void testGetHealth() {
    System.out.println("getHealth");
    given().expect().statusCode(200).body("isHealthy", equalTo(true)).when().get("/health");

  }

}
