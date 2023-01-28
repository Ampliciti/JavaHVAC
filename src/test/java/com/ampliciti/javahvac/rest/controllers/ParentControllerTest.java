/*
 * Copyright (C) 2019 jeffrey
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ampliciti.javahvac.rest.controllers;

import com.ampliciti.javahvac.Main;
import com.ampliciti.javahvac.ParentNodeTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author jeffrey
 */
public abstract class ParentControllerTest extends ParentNodeTest {

  private static Thread mainThread;

  @BeforeClass
  public static void setUpClassController() throws InterruptedException {
    startMocks();// start up our mock nodes
    Thread.sleep(2000);// for some reason the mocks take a bit to come up online
    String[] args = {"./config-samples/server.yaml.sample-network-test"};
    // start it up in a thread so we can kill it when we're done with the test
    mainThread = new Thread() {
      @Override
      public void run() {
        Main.main(args);
      }

    };
    mainThread.start();
    Thread.sleep(2000);// let the service startup
  }

  @AfterClass
  public static void tearDownClassController() {
    stopMocks();
    mainThread.stop();// not safe, but it's just a unit test

  }

  @Before
  public void setUp() {
    ;// do not do the normal tear down of mocks; they need to stay up for this set of tests
  }

  @After
  public void tearDown() {
    ;// do not do the normal tear down of mocks; they need to stay up for this set of tests
  }
}
