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
package com.ampliciti.javahvac.rest.controllers;

import com.ampliciti.javahvac.config.OverrideHolder;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.domain.MiscNotices;
import com.ampliciti.javahvac.service.DaylightService;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * REST Controller for getting the current system status.
 * 
 * @author jeffrey
 */
public class StatusController {

  private static Logger logger = Logger.getLogger(StatusController.class);

  public Object read(Request request, Response response) {
    try {
      logger.info("/status called");
      // // Construct the response for create...
      response.setResponseStatus(HttpResponseStatus.OK);
      response.setContentType(ContentType.JSON);
      Map toReturn = new HashMap();
      List currentState = new ArrayList(CurrentNodeState.getCurrentNodeState().values());
      logger.debug("Status is returning current node state of: " + Arrays.toString(currentState.toArray()));
      toReturn.put("nodeState", currentState);
      String cisternNotices = MiscNotices.getCisternNotice();
      if (cisternNotices != null) {
        toReturn.put("cisternStatus", cisternNotices);
      }
      toReturn.put("sun", DaylightService.getDayLight().asMap());
      toReturn.put("overrides", OverrideHolder.getAllOverrides());
      return toReturn;
    } catch (Exception e) {
      logger.error("Problem performing action", e);
      response.setResponseStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
      // response.setBody("Problem getting status: " + e.getMessage());
      response.setContentType(ContentType.JSON);
      return "Problem getting status: " + e.getMessage();
    }
  }

}
