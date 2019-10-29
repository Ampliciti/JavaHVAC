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
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.exceptions.NodeConnectionException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import org.mockserver.verify.VerificationTimes;

/**
 *
 * @author jeffrey
 */
public class NodeCommandRESTDaoTest extends ParentNodeTest {

  public NodeCommandRESTDaoTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of sendCommand method, of class NodeCommandRESTDao.
   */
  @Test
  public void testSendCommandSuccessTrue() throws Exception {
    System.out.println("sendCommandSuccessTrue");
    // setup the mock
    int testPort = 8081;
    ClientAndServer mockServer = ClientAndServer.startClientAndServer(testPort);
    try {
      mockServer
          .when(request().withPath("/action").withBody(exact("{\"name\":\"mill\",\"state\":true}")))
          .respond(response().withBody("{\"name\":\"mill\",\"state\":true}").withStatusCode(201));
      VerificationTimes.exactly(1);
      String nodeAddress = "localhost:" + testPort;
      String zone = "mill";
      boolean command = true;
      NodeCommandRESTDao instance = new NodeCommandRESTDao();
      boolean expResult = true;
      boolean result = instance.sendCommand(nodeAddress, zone, command);
      assertEquals(expResult, result);
    } finally {
      stopMock(mockServer);
    }
  }

  /**
   * Test of sendCommand method, of class NodeCommandRESTDao.
   */
  @Test
  public void testSendCommandSuccessFalse() throws Exception {
    System.out.println("sendCommandSuccessFalse");
    // setup the mock
    int testPort = 8081;
    ClientAndServer mockServer = ClientAndServer.startClientAndServer(testPort);
    try {
      mockServer
          .when(
              request().withPath("/action").withBody(exact("{\"name\":\"mill\",\"state\":false}")))
          .respond(response().withBody("{\"name\":\"mill\",\"state\":false}").withStatusCode(201));
      VerificationTimes.exactly(1);
      String nodeAddress = "localhost:" + testPort;
      String zone = "mill";
      boolean command = false;
      NodeCommandRESTDao instance = new NodeCommandRESTDao();
      boolean expResult = true;
      boolean result = instance.sendCommand(nodeAddress, zone, command);
      assertEquals(expResult, result);
    } finally {
      stopMock(mockServer);
    }
  }

  /**
   * Test of sendCommand method, of class NodeCommandRESTDao.
   */
  @Test
  public void testSendCommandFailToChange() throws Exception {
    System.out.println("sendCommandFailToChange");
    // setup the mock
    int testPort = 8081;
    ClientAndServer mockServer = ClientAndServer.startClientAndServer(testPort);
    try {
      mockServer
          .when(request().withPath("/action").withBody(exact("{\"name\":\"mill\",\"state\":true}")))
          .respond(response().withBody("{\"name\":\"mill\",\"state\":false}").withStatusCode(201));
      VerificationTimes.exactly(1);
      String nodeAddress = "localhost:" + testPort;
      String zone = "mill";
      boolean command = true;
      NodeCommandRESTDao instance = new NodeCommandRESTDao();
      boolean expResult = false;
      boolean result = instance.sendCommand(nodeAddress, zone, command);
      assertEquals(expResult, result);
    } finally {
      stopMock(mockServer);
    }
  }

  /**
   * Test of sendCommand method, of class NodeCommandRESTDao.
   */
  @Test(expected = NodeConnectionException.class)
  public void testSendCommandException() throws NodeConnectionException {
    System.out.println("sendCommandException");
    // setup the mock
    int testPort = 8081;
    ClientAndServer mockServer = ClientAndServer.startClientAndServer(testPort);
    try {
      mockServer
          .when(request().withPath("/action").withBody(exact("{\"name\":\"mill\",\"state\":true}")))
          .respond(response().withBody("{\"error\":\"botched!\"}").withStatusCode(400));
      VerificationTimes.exactly(1);
      String nodeAddress = "localhost:" + testPort;
      String zone = "mill";
      boolean command = true;
      NodeCommandRESTDao instance = new NodeCommandRESTDao();
      instance.sendCommand(nodeAddress, zone, command);// should throw an exception
    } finally {
      stopMock(mockServer);
    }
  }

}
