/*
 * Copyright (C) 2019-2022 jeffrey
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
package com.ampliciti.javahvac.rest;

import com.ampliciti.javahvac.rest.controllers.HealthCheckController;
import com.ampliciti.javahvac.rest.controllers.RegionOverrideController;
import com.ampliciti.javahvac.rest.controllers.SourceOverrideController;
import com.ampliciti.javahvac.rest.controllers.StatusCleanController;
import com.ampliciti.javahvac.rest.controllers.StatusController;
import com.ampliciti.javahvac.rest.controllers.ZoneController;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpMethod.PUT;

import org.restexpress.RestExpress;

/**
 *
 * @author jeffrey
 */
public abstract class Routes {

  public static void define(HealthCheckController hcc, StatusController sc, StatusCleanController scc,
      ZoneController zc, SourceOverrideController soc, RegionOverrideController roc, RestExpress server) {
    // health check
    server.uri("/health", hcc).action("getHealth", GET).name("health").noSerialization();

    /**
     * route to get the current state of everything
     */
    server.uri("/status", sc).method(GET).name("status");

    /**
     * route to get the current state of everything sorted by zone in an easier to read format
     */
    server.uri("/statusClean", scc).method(GET).name("statusClean");

    /**
     * route to modify a zone state
     */
    server.uri("/zone", zc).method(POST, PUT).name("zone");

    /**
     * route to set an source override
     */
    server.uri("/sourceOverride", soc).method(POST, PUT, GET).name("sourceOverride");

    /**
     * route to set an region override
     */
    server.uri("/regionOverride", roc).method(POST, PUT, GET).name("regionOverride");

  }
}
