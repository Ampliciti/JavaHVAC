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
package com.ampliciti.javahvac;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Commonly needed util methods.
 *
 * @author jeffrey
 */
public class Utils {

  private static final String HTTP_PROTOCOL = "http";
  private static final int DEFAULT_PORT = 80;

  /**
   * Converts a string address from our config file into a Java usable URL object. There are better
   * ways to do this, esp with exception handling, but winter is literally coming and I need this
   * code working soon to heat my house. Don't judge me.
   *
   * @param address String of the address of a node -- expects no protocol with port being optional
   * @return Java URL object
   */
  public static URL buildUrlFromAddressString(String address) {
    try {
      String[] splits = address.split("\\Q:\\E");
      if (splits.length == 1) {
        return new URL(HTTP_PROTOCOL, address, DEFAULT_PORT, "");
      } else if (splits.length == 2) {
        return new URL(HTTP_PROTOCOL, splits[0], Integer.parseInt(splits[1]), "");
      } else {
        throw new Exception("Node address not in the correct format.");
      }
    } catch (Exception e) {
      throw new RuntimeException("The address passed in was not in a correct format: " + address
          + ". Expected format is: [IP|DNS]:port", e);
    }
  }

  /**
   * Converts dates from the Sun API (https://api.sunrise-sunset.org/) to milliseconds UTC.
   * 
   * @param dateStr String in the format of 2019-12-28T14:18:30+00:00.
   * @return MS since epoc.
   * @throws ParseException If the date cannot be read/translated/etc.
   */
  public static long fromISO8601UTC(String dateStr) throws ParseException {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    df.setTimeZone(tz);
    return df.parse(dateStr).getTime();
  }

}
