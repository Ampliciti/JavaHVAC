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

import io.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * Simple health check controller.
 * 
 * @author jeffrey
 */
public class HealthCheckController {
  public Object getHealth(Request request, Response response) {
    response.setResponseStatus(HttpResponseStatus.OK);
    response.setContentType(ContentType.JSON);
    return "{\"isHealthy\":true}";
  }
}
