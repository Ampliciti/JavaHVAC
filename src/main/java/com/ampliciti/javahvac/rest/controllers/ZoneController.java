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

import com.ampliciti.javahvac.exceptions.NodeConnectionException;
import com.ampliciti.javahvac.exceptions.PermissionsException;
import com.ampliciti.javahvac.service.NodeService;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.log4j.Logger;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * Route for modifying a name state directly.
 *
 * @author jeffrey
 */
public class ZoneController {

  private NodeService service;
  /**
   * Logger for this class.
   */
  private static Logger logger = Logger.getLogger(ZoneController.class);

  public ZoneController() {
    service = new NodeService();
  }

  /**
   * POST call.
   *
   * @param request
   * @param response
   * @return
   */
  public Object create(Request request, Response response) {
    ChangeRequest cr = request.getBodyAs(ChangeRequest.class);
    try {
      if (service.endUserChangeZoneState(cr.getName(), cr.getState())) {
        response.setResponseStatus(HttpResponseStatus.CREATED);
        return cr;
      } else {
        response.setResponseStatus(HttpResponseStatus.EXPECTATION_FAILED);
        return "The node refused to change state.";
      }

    } catch (NodeConnectionException e) {
      String error = "Could not set node state because it is offline or malfunctioning. " + cr.toString();
      logger.error(error, e);
      response.setResponseStatus(HttpResponseStatus.SERVICE_UNAVAILABLE);
      return error;
    } catch (PermissionsException e) {
      String error = "This zone is not allowed to be manually changed. " + cr.toString();
      logger.error(error, e);
      response.setResponseStatus(HttpResponseStatus.FORBIDDEN);
      return error;
    }

  }

  /**
   * PUT call
   *
   * @param request
   * @param response
   * @return
   */
  public Object update(Request request, Response response) {
    return create(request, response);
  }

  /**
   * POJO for request.
   */
  private static final class ChangeRequest {

    /**
     * Name of zone/area to change.
     */
    private String name;
    /**
     * State that the named zone should be set to.
     */
    private boolean state;

    /**
     * Default constructor; needed for serialization.
     */
    public ChangeRequest() {}

    /**
     *
     * @param name Name of zone/area to change.
     * @param state State that the named zone should be set to.
     */
    public ChangeRequest(String name, boolean state) {
      this.name = name;
      this.state = state;
    }

    /**
     * Zone name to change.
     *
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * Zone name to change.
     *
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * State that the named zone should be set to.
     *
     * @return the state
     */
    public boolean getState() {
      return state;
    }

    /**
     * State that the named zone should be set to.
     *
     * @param state the state to set
     */
    public void setState(boolean state) {
      this.state = state;
    }

    @Override
    public String toString() {
      return "ChangeRequest{" + "name=" + name + ", state=" + state + '}';
    }

  }
}
