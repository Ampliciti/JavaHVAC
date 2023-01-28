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
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.Utils;
import com.ampliciti.javahvac.dao.RESTDao;
import com.ampliciti.javahvac.dao.domain.DayLight;
import com.ampliciti.javahvac.domain.config.Location;
import com.ampliciti.javahvac.exceptions.RESTException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import org.json.simple.JSONObject;

/**
 * DAO for fetching daylight times from the REST API at https://api.sunrise-sunset.org/
 * 
 * @author jeffrey
 */
public class DayLightDao {

  /**
   * Gets daylight hours from https://api.sunrise-sunset.org/
   * 
   * @param location
   * @return
   */
  public DayLight getDayLight(Location location) throws RESTException {
    JSONObject response = null;
    try {
      URL sunApi = new URL("https://api.sunrise-sunset.org/");
      RESTDao restDao = new RESTDaoImpl(sunApi);
      StringBuilder path = new StringBuilder("/json?lat=");
      path.append(location.getLatitude());
      path.append("&lng=");
      path.append(location.getLongitude());
      path.append("&date=today&formatted=0");
      response = restDao.doGetCall(path.toString(), true);
      JSONObject results = (JSONObject) response.get("results");
      String sunrise = (String) results.get("sunrise");
      String sunset = (String) results.get("sunset");
      DayLight toReturn = new DayLight(Utils.fromISO8601UTC(sunrise), Utils.fromISO8601UTC(sunset));
      return toReturn;
    } catch (MalformedURLException e) {
      throw new RuntimeException("Programming mistake; this should never happen. The Sun API URL is wrong.", e);
    } catch (ParseException e) {
      if (response != null) {
        throw new RESTException(response, e);
      } else {
        throw new RESTException("Problem processing call to Sun API", e);
      }

    }

  }
}
