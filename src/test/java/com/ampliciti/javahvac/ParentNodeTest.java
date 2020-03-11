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
package com.ampliciti.javahvac;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * Parent class for any test that does node mocking.
 *
 * @author jeffrey
 */
public class ParentNodeTest {

  protected static Logger logger = Logger.getLogger(ParentNodeTest.class);

  protected static String barnResponse;
  protected static String atticResponse;
  protected static String centralResponse;
  protected static String cisternResponse;

  protected static ClientAndServer mockServerBarn = null;

  protected static ClientAndServer mockServerCentral = null;

  protected static ClientAndServer mockServerAttic = null;

  protected static ClientAndServer mockServerCistern = null;

  @BeforeClass
  public static void setUpClass() throws IOException {
    barnResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/barn-node-info.json"), "UTF-8");
    atticResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/attic-node-info.json"), "UTF-8");
    centralResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/central-node-info.json"), "UTF-8");

    cisternResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/cistern-node-info.json"), "UTF-8");
  }

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {
    stopMocks();// kill our mocks in case they're still running
  }

  @After
  public void tearDown() {
    stopMocks();// kill our mocks in case they're still running
  }

  protected static void startMocks() {
    logger.debug("Starting mock nodes...");
    try {
      if (mockServerBarn == null || !mockServerBarn.isRunning()) {
        mockServerBarn = ClientAndServer.startClientAndServer(8082);
        mockServerBarn.when(request().withPath("/info"))
            .respond(response().withBody(barnResponse).withStatusCode(200));
      }
      if (mockServerCentral == null || !mockServerCentral.isRunning()) {
        mockServerCentral = ClientAndServer.startClientAndServer(8083);
        mockServerCentral.when(request().withPath("/info"))
            .respond(response().withBody(centralResponse).withStatusCode(200));
      }
      if (mockServerAttic == null || !mockServerAttic.isRunning()) {
        mockServerAttic = ClientAndServer.startClientAndServer(8084);
        mockServerAttic.when(request().withPath("/info"))
            .respond(response().withBody(atticResponse).withStatusCode(200));
      }
      if (mockServerCistern == null || !mockServerCistern.isRunning()) {
        mockServerCistern = ClientAndServer.startClientAndServer(8085);
        mockServerCistern.when(request().withPath("/info"))
            .respond(response().withBody(cisternResponse).withStatusCode(200));
      }
    } catch (RuntimeException e) {
      // try {
      // Thread.sleep(1000);
      // } catch (InterruptedException interruppted) {
      // Thread.currentThread().isInterrupted();//eh.
      // }
      // logger.debug("Mock service already in use by something? Waiting and trying again.");
      // startMocks();//recurse
      //^^ comment back in to assist with troubleshooting if needed
      throw e;
    }
  }

  protected static void stopMocks() {
    logger.debug("Stopping mock nodes.");
    stopMock(mockServerBarn);
    stopMock(mockServerCentral);
    stopMock(mockServerAttic);
    stopMock(mockServerCistern);
    // try {
    // Thread.sleep(500);
    // } catch (Exception e) {
    // Thread.currentThread().isInterrupted();
    // }
  }

  protected static void stopMock(ClientAndServer mockToStop) {
    if (mockToStop != null) {
      mockToStop.stop();
      while (mockToStop.isRunning()) {
        logger.debug("Waiting for mock to stop.");
      }
    }

  }

}
