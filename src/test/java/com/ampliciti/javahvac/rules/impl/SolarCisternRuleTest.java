/*
 * Copyright (C) 2019-2020 jeffrey
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
import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.dao.domain.DayLight;
import com.ampliciti.javahvac.dao.domain.SourceOverride;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.MiscNotices;
import com.ampliciti.javahvac.service.DaylightService;
import java.io.File;
import java.lang.reflect.Field;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import org.mockserver.verify.VerificationTimes;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

/**
 *
 * @author jeffrey
 */
@RunWith(PowerMockRunner.class) // needed for mocking daylight
@PowerMockRunnerDelegate(JUnit4ClassRunner.class)
@PrepareForTest(DaylightService.class) // needed for mocking daylight
@PowerMockIgnore("javax.net.ssl.*") // needed for mocking daylight
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
   * Mocks the daylight service so that it will return what you want it to. Allow for coding after
   * dark.
   * 
   * @param dark If true, returns a nighttime daylight. If false, returns a daytime daylight.
   */
  private static void mockDayLight(boolean dark) {
    PowerMockito.mockStatic(DaylightService.class);
    DateTime nowDateTime = new DateTime(); // Gives the default time zone.
    DateTime dateTime = nowDateTime.toDateTime(DateTimeZone.UTC); // Converting default zone to UTC
    long currentTime = dateTime.getMillis();
    DayLight daylight;
    if (dark) { // make it night
      daylight = new DayLight(currentTime + 45 * 60000, currentTime + 43200000); // sunrises in 45
                                                                                 // minutes, and
                                                                                 // sets in 12 hours

    } else { // make it day
      daylight = new DayLight(currentTime - 45 * 60000, currentTime + 45 * 60000);
      // make it think that the sun rose 45 minutes ago and will set in 45 minutes -- i guess we're
      // on
      // a spinning astroid?
    }

    BDDMockito.given(DaylightService.getDayLight()).willReturn(daylight);
    // end daylight mock -- a heck of a lot of work for this stupid problem

  }

  /**
   * Test of enforceRule method, of class SolarCisternRule.
   */
  @Test
  public synchronized void testEnforceRuleTooCold() throws Exception {
    System.out.println("testEnforceRuleTooCold");

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
    // hack the instance with reflection so we don't wait minutes to return
    Field maxColdRuntimeField = SolarCisternRule.class.getDeclaredField("maxColdRuntime");
    Field retryTimeField = SolarCisternRule.class.getDeclaredField("retryTime");
    maxColdRuntimeField.setAccessible(true);
    retryTimeField.setAccessible(true);
    maxColdRuntimeField.set(instance, 5000);
    retryTimeField.set(instance, 15000);
    // end hack

    // mock the daylight service so it thinks it is daylight (even if you're coding this well after
    mockDayLight(false);

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
    String recircOnCisternResponse = cisternResponse.replace(
        "        {\n" + "            \"source\": \"cistern\",\n" + "            \"state\": false,\n"
            + "            \"name\": \"recirculatorPump\"\n" + "        }",
        "        {\n" + "            \"source\": \"cistern\",\n" + "            \"state\": true,\n"
            + "            \"name\": \"recirculatorPump\"\n" + "        }");
    logger.debug("recircOnCisternResponse: " + recircOnCisternResponse);
    mockServerCistern.stop();
    mockServerCistern = ClientAndServer.startClientAndServer(8085);// this is kinda ugly; not sure
                                                                   // why i have to restart this
    mockServerCistern.when(request().withPath("/info"))
        .respond(response().withBody(recircOnCisternResponse).withStatusCode(200));

    // wait
    Thread.sleep(16000);
    CurrentNodeState.refreshNodeState();// build our registry of nodes again (with the new cistern
                                        // running state)
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
    assertEquals("Recirculator off: Not enough incoming heat. " + -1.125, cisternStatus);


  }

  /**
   * Test of enforceRule method, of class SolarCisternRule.
   */
  @Test
  public synchronized void testEnforceRuleTooColdWithOverrideCycle() throws Exception {
    System.out.println("testEnforceRuleTooColdWithOverrideCycle");

    // setup
    File yamlFile = new File("./config-samples/server.yaml.sample-network-test");
    if (!yamlFile.exists()) {
      fail("Bad test setup; " + yamlFile.getAbsolutePath() + " does not exist.");
    }
    ServerConfig.buildConfig(yamlFile);
    // mock
    startMocks();

    CurrentNodeState.refreshNodeState();// build our registry of nodes

    OverrideHolder.setSourceOverride("cistern", SourceOverride.OVERRIDE_ON);

    // end setup
    SolarCisternRule instance = new SolarCisternRule("Cistern", 120);
    // hack the instance with reflection so we don't wait minutes to return
    Field maxColdRuntimeField = SolarCisternRule.class.getDeclaredField("maxColdRuntime");
    Field retryTimeField = SolarCisternRule.class.getDeclaredField("retryTime");
    maxColdRuntimeField.setAccessible(true);
    retryTimeField.setAccessible(true);
    maxColdRuntimeField.set(instance, 5000);
    retryTimeField.set(instance, 15000);
    // end hack

    // mock the daylight service so it thinks it is daylight (even if you're coding this well after
    // dark)
    mockDayLight(false);

    // override on
    // make sure the cistern gets turned on
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":true}")
            .withStatusCode(201));

    VerificationTimes.exactly(1);
    boolean expResult = true;
    boolean result = true;
    for (int i = 0; i < 5; i++) {
      MiscNotices.setCisternNotice(null);
      result = instance.enforceRule();
      assertEquals(expResult, result);
      assertEquals(65.975, instance.getCisternBottomTemp(), .0001);
      assertEquals(64.85, instance.getCisternInletTemp(), .0001);
      assertEquals(65.975, instance.getCisternTopTemp(), .0001);
      assertEquals(65.975, instance.getCisternAverageTemp(), .0001);
      assertEquals(-1.125, instance.getTempGain(), .0001);
      String cisternStatus = MiscNotices.getCisternNotice();
      assertNotNull(cisternStatus);
      assertEquals("Recirculator Manual Override: ON. Temperature gain is: " + -1.125,
          cisternStatus);
      Thread.sleep(500);
    }
    // override off
    // make sure the cistern gets turned off
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":true}")
            .withStatusCode(201));

    VerificationTimes.exactly(1);
    OverrideHolder.setSourceOverride("cistern", SourceOverride.OVERRIDE_OFF);
    for (int i = 0; i < 5; i++) {
      MiscNotices.setCisternNotice(null);
      result = instance.enforceRule();
      assertEquals(expResult, result);
      assertEquals(65.975, instance.getCisternBottomTemp(), .0001);
      assertEquals(64.85, instance.getCisternInletTemp(), .0001);
      assertEquals(65.975, instance.getCisternTopTemp(), .0001);
      assertEquals(65.975, instance.getCisternAverageTemp(), .0001);
      assertEquals(-1.125, instance.getTempGain(), .0001);
      String cisternStatus = MiscNotices.getCisternNotice();
      assertNotNull(cisternStatus);
      assertEquals("Recirculator Manual Override: OFF.", cisternStatus);
      Thread.sleep(500);
    }
    // override run
    OverrideHolder.setSourceOverride("cistern", SourceOverride.RUN);
    // run the full too cold test as part of this test to make sure we didn't botch something up
    testEnforceRuleTooCold();
  }


  /**
   * Test of enforceRule method, of class SolarCisternRule.
   */
  @Test
  public synchronized void testEnforceRuleDayToNightToDay() throws Exception {
    System.out.println("testEnforceRuleDayToNightToDay");

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
    // hack the instance with reflection so we don't wait minutes to return
    Field maxColdRuntimeField = SolarCisternRule.class.getDeclaredField("maxColdRuntime");
    Field retryTimeField = SolarCisternRule.class.getDeclaredField("retryTime");
    maxColdRuntimeField.setAccessible(true);
    retryTimeField.setAccessible(true);
    maxColdRuntimeField.set(instance, 5000);
    retryTimeField.set(instance, 15000);
    // end hack

    // make it think it is night
    mockDayLight(true);

    // our current rules would require the cistern to be turned off; make sure that happens
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":false}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":false}")
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
    assertEquals("Recirculator off: Night.", cisternStatus);

    // wait
    Thread.sleep(16000);
    CurrentNodeState.refreshNodeState();// build our registry of nodes again

    // make it think it is daytime
    mockDayLight(false);

    // make sure the cistern gets turned on
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":true}")
            .withStatusCode(201));
    VerificationTimes.exactly(1);
    result = instance.enforceRule();
    assertEquals(expResult, result);
    // make sure it tries to start up the cistern
    cisternStatus = MiscNotices.getCisternNotice();
    assertNotNull(cisternStatus);
    assertEquals("Running to see if we have more heat. Temperature gain is: " + -1.125,
        cisternStatus);

    // wait
    Thread.sleep(16000);
    CurrentNodeState.refreshNodeState();// build our registry of nodes again


    // make it think it is night again
    mockDayLight(true);
    // make sure the cistern gets turned off
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":false}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":false}")
            .withStatusCode(201));
    VerificationTimes.exactly(1);
    result = instance.enforceRule();
    assertEquals(expResult, result);


    // wait
    Thread.sleep(16000);
    CurrentNodeState.refreshNodeState();// build our registry of nodes again

    // make it think it is daytime yet again
    mockDayLight(false);

    // make sure the cistern gets turned on
    super.mockServerCistern
        .when(request().withPath("/action")
            .withBody(exact("{\"name\":\"recirculatorPump\",\"state\":true}")))
        .respond(response().withBody("{\"name\":\"recirculatorPump\",\"state\":true}")
            .withStatusCode(201));
    VerificationTimes.exactly(1);
    result = instance.enforceRule();
    assertEquals(expResult, result);
    // make sure it tries to start up the cistern
    cisternStatus = MiscNotices.getCisternNotice();
    assertNotNull(cisternStatus);
    assertEquals("Running to see if we have more heat. Temperature gain is: " + -1.125,
        cisternStatus);
  }

}
