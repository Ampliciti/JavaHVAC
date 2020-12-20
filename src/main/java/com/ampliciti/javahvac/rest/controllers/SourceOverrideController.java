/*
 * Copyright (C) 2020 jeffrey
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

import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.dao.domain.SourceOverride;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.log4j.Logger;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * Route for modifying a name state directly.
 *
 * @author jeffrey
 */
public class SourceOverrideController {


  /**
   * Logger for this class.
   */
  private static Logger logger = Logger.getLogger(SourceOverrideController.class);

  public SourceOverrideController() {

  }


  // /**
  // * GET call.
  // * @param request
  // * @param response
  // * @return
  // */
  // public Object read(Request request, Response response) {
  //
  // logger.info("get overrides called");//TODO replace with route name
  //
  // }

  /**
   * POST call.
   *
   * @param request
   * @param response
   * @return
   */
  public Object create(Request request, Response response) {
    OverrideRequest cr = request.getBodyAs(OverrideRequest.class);
    try {
      logger.info("Call to /sourceOverride " + cr.toString());
      OverrideHolder.setSourceOverride(cr.getName(), cr.getState());// TODO: This isn't really safe
                                                                    // as a caller could just start
                                                                    // setting random keys here, but
                                                                    // I'm just running this on my
                                                                    // LAN right now, and I don't
                                                                    // care. This would be the
                                                                    // world's most inefficent
                                                                    // trinary data store for an
                                                                    // hacker.
      response.setResponseStatus(HttpResponseStatus.CREATED);
      return cr;
    } catch (Exception e) { // this really shouldn't happen
      String error = "Could not set override." + cr.toString();
      logger.error(error, e);
      response.setResponseStatus(HttpResponseStatus.SERVICE_UNAVAILABLE);
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
  private static final class OverrideRequest {

    /**
     * Name of source to override.
     */
    private String name;

    /**
     * State to change the named source to.
     */
    private SourceOverride state;

    /**
     * Default constructor; needed for serialization.
     */
    public OverrideRequest() {}

    /**
     *
     * @param name Name of source to change.
     * @param state State to change the named source to.
     */
    public OverrideRequest(String name, SourceOverride state) {
      this.name = name;
      this.state = state;
    }

    /**
     * Name of source to change.
     *
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * Name of source to change.
     *
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * State to change the named source to.
     *
     * @return the state
     */
    public SourceOverride getState() {
      return state;
    }

    /**
     * State to change the named source to.
     *
     * @param state the state to set
     */
    public void setState(SourceOverride state) {
      this.state = state;
    }

    @Override
    public String toString() {
      return "OverrideRequest{" + "name=" + name + ", state=" + state + '}';
    }

  }
}
