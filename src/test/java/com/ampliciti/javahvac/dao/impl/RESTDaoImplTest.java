/*
 * Copyright (C) 2018 jeffrey
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
package com.ampliciti.javahvac.dao.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffrey
 */
public class RESTDaoImplTest {

  private static Server jettyServer;
  private static final int testPort = 8888;

  public RESTDaoImplTest() {}

  @BeforeClass
  public static void setUpJetty() throws Exception {

    jettyServer = new Server(testPort);

    Handler handler = new AbstractHandler() {
      @Override
      public void handle(String target, Request request, HttpServletRequest servletRequest,
          HttpServletResponse response) throws IOException, ServletException {
        handleHttpRequest(target, request, response);
      }
    };

    // returnUrl = "<a class="vglnk" href="http://localhost"
    // rel="nofollow"><span>http</span><span>://</span><span>localhost</span></a>:" +
    // Integer.toString(testPort) + "/test";
    jettyServer.setHandler(handler);

    jettyServer.start();
  }

  public static void handleHttpRequest(String target, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // stuff here
    try (PrintWriter pw = response.getWriter()) {
      pw.append("{\"isHealthy\":true}");// just return something
      pw.flush();
    }
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    ((Request) request).setHandled(true);
  }

  @AfterClass
  public static void tearDownJetty() throws Exception {
    jettyServer.stop();
  }

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getHost method, of class RESTDaoImpl.
   */
  @Test
  public void testGetHost() throws Exception {
    System.out.println("getHost");
    RESTDaoImpl instance = new RESTDaoImpl(new URL("http://localhost:" + testPort + "/"));
    URL result = instance.getHost();
    assertEquals("http://localhost:" + testPort + "/", result.toExternalForm());
  }

  /**
   * Test of doGetCall method, of class RESTDaoImpl.
   */
  @Test
  public void testDoGetCall() throws Exception {
    System.out.println("doGetCall");
    String path = "/health";
    RESTDaoImpl instance = new RESTDaoImpl(new URL("http://localhost:" + testPort + "/"));
    JSONObject result = instance.doGetCall(path);
    assertEquals("{\"isHealthy\":true}", result.toJSONString());
  }

  /**
   * Test of doPostCall method, of class RESTDaoImpl.
   */
  @Test
  public void testDoPostCall_String_JSONObject() throws Exception {
    System.out.println("doPostCall");
    String path = "/whatever";
    JSONParser parser = new JSONParser();
    JSONObject toPost = (JSONObject) parser.parse("{\"randomData\":true}");
    RESTDaoImpl instance = new RESTDaoImpl(new URL("http://localhost:" + testPort + "/"));
    JSONAware result = instance.doPostCall(path, toPost);
    assertEquals("{\"isHealthy\":true}", result.toJSONString());
  }

  /**
   * Test of doPostCall method, of class RESTDaoImpl.
   */
  @Test
  public void testDoPostCall_3args() throws Exception {
    System.out.println("doPostCall");
    String path = "/whatever";
    JSONParser parser = new JSONParser();
    JSONObject toPost = (JSONObject) parser.parse("{\"randomData\":true}");
    HashMap<String, String> headers = null;
    RESTDaoImpl instance = new RESTDaoImpl(new URL("http://localhost:" + testPort + "/"));
    JSONAware result = instance.doPostCall(path, toPost, headers);
    assertEquals("{\"isHealthy\":true}", result.toJSONString());
  }

  /**
   * Test of doPutCall method, of class RESTDaoImpl.
   */
  @Test
  public void testDoPutCall() throws Exception {
    System.out.println("doPutCall");
    String path = "/whatever";
    JSONParser parser = new JSONParser();
    JSONObject toPost = (JSONObject) parser.parse("{\"randomData\":true}");
    RESTDaoImpl instance = new RESTDaoImpl(new URL("http://localhost:" + testPort + "/"));
    JSONObject result = instance.doPutCall(path, toPost);
    assertEquals("{\"isHealthy\":true}", result.toJSONString());
  }

  /**
   * Test of doDeleteCall method, of class RESTDaoImpl.
   */
  @Test
  public void testDoDeleteCall() throws Exception {
    System.out.println("doDeleteCall");
    String path = "/whatever";
    RESTDaoImpl instance = new RESTDaoImpl(new URL("http://localhost:" + testPort + "/"));
    instance.doDeleteCall(path);
  }

}
