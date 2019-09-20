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

import com.ampliciti.javahvac.rest.controllers.StatusController;
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

  private static Logger logger = Logger.getLogger(ParentNodeTest.class);

  private static String barnResponse;
  private static String atticResponse;
  private static String centralResponse;

  protected static ClientAndServer mockServerBarn = null;

  protected static ClientAndServer mockServerCentral = null;

  protected static ClientAndServer mockServerAttic = null;

  @BeforeClass
  public static void setUpClass() throws IOException {
    barnResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/barn-node-info.json"), "UTF-8");
    atticResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/attic-node-info.json"), "UTF-8");
    centralResponse = FileUtils
        .readFileToString(new File("./config-samples/node-json/central-node-info.json"), "UTF-8");
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
    logger.debug("Starting mock nodes....");
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
  }

  private static void stopMocks() {
    logger.debug("Stopping mock nodes.");
    stopMock(mockServerBarn);
    stopMock(mockServerCentral);
    stopMock(mockServerAttic);
  }

  protected static void stopMock(ClientAndServer mockToStop) {
    if (mockToStop != null) {
      mockToStop.stop();
    }
  }

}
