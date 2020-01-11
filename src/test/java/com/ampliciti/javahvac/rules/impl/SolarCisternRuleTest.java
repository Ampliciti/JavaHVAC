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
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.ParentNodeTest;
import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.MiscNotices;
import java.io.File;
import java.lang.reflect.Field;
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
public class SolarCisternRuleTest extends ParentNodeTest {

  public SolarCisternRuleTest() {}

  /**
   * Test of getDefinition method, of class SolarCisternRule.
   */
  @Test
  public void testGetDefinition() {
    System.out.println("getDefinition");
    SolarCisternRule instance = new SolarCisternRule("Cistern", 140);
    String expResult = "Solar cistern rule for: Cistern";
    String result = instance.getDefinition();
    assertEquals(expResult, result);
  }

  /**
   * Test of enforceRule method, of class SolarCisternRule.
   */
  @Test
  public void testEnforceRule() throws Exception {
    System.out.println("enforceRule");

    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    CurrentNodeState.refreshNodeState();// build our registry of nodes

    // end setup
    SolarCisternRule instance = new SolarCisternRule("Cistern", 120);
    // hack the instance with reflection so we don't want minutes to return
    Field maxColdRuntimeField = SolarCisternRule.class.getDeclaredField("maxColdRuntime");
    Field retryTimeField = SolarCisternRule.class.getDeclaredField("retryTime");
    maxColdRuntimeField.setAccessible(true);
    retryTimeField.setAccessible(true);
    maxColdRuntimeField.set(instance, 5000);
    retryTimeField.set(instance, 15000);
    // end hack

    // our current rules would require the cistern to be turned on; make sure that happens
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":true}")
            .withStatusCode(201));

    VerificationTimes.exactly(1);
    MiscNotices.setCisternNotice(null);
    boolean expResult = true;
    boolean result = instance.enforceRule();
    assertEquals(expResult, result);
    assertEquals(65.975, instance.getCisternBottomTemp(), .0001);
    assertEquals(64.85, instance.getCisternInletTemp(), .0001);
    assertEquals(65.975, instance.getCisternTopTemp(), .0001);
    assertEquals(65.975, instance.getCisternAverageTemp(), .0001);
    assertEquals(-1.125, instance.getTempGain(), .0001);
    String cisternStatus = MiscNotices.getCisternNotice();
    assertNotNull(cisternStatus);
    assertEquals("Running to see if we have more heat. Temperature gain is: " + -1.125,
        cisternStatus);

    // do some more mocking (indicating our cistern pump started)
    String recicOnCisternResponse = cisternResponse.replace(
        "        {\n" + "            \"source\": \"cistern\",\n" + "            \"state\": false,\n"
            + "            \"name\": \"recirculatorPump\"\n" + "        }",
        "        {\n" + "            \"source\": \"cistern\",\n" + "            \"state\": true,\n"
            + "            \"name\": \"recirculatorPump\"\n" + "        }");
    logger.debug("recicOnCisternResponse: " + recicOnCisternResponse);
    mockServerCistern.stop();
    mockServerCistern = ClientAndServer.startClientAndServer(8085);//this is kinda ugly; not sure why i have to restart this
    mockServerCistern.when(request().withPath("/info"))
        .respond(response().withBody(recicOnCisternResponse).withStatusCode(200));

    // wait
    Thread.sleep(16000);
    CurrentNodeState.refreshNodeState();// build our registry of nodes again (with the new cistern running state)
    // run again -- nothing's changed temp wise

    // however, at this point, we're giving up that it's going to get better
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":false}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":false}")
            .withStatusCode(201));
    
    result = instance.enforceRule();
    assertEquals(expResult, result);
    assertEquals(65.975, instance.getCisternBottomTemp(), .0001);
    assertEquals(64.85, instance.getCisternInletTemp(), .0001);
    assertEquals(65.975, instance.getCisternTopTemp(), .0001);
    assertEquals(65.975, instance.getCisternAverageTemp(), .0001);
    assertEquals(-1.125, instance.getTempGain(), .0001);
    cisternStatus = MiscNotices.getCisternNotice();
    assertNotNull(cisternStatus);
    assertEquals("Reciculator off: Not enough Sun. " + -1.125, cisternStatus);


  }
}
