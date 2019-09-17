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
package com.ampliciti.javahvac;

import java.net.URL;

/**
 * Commonly needed util methods.
 * 
 * @author jeffrey
 */
public class Utils {

  private static final String HTTP_PROTOCOL = "http";
  private static final int DEFAULT_PORT = 80;

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
      // there's better ways to do this, but winter is literally coming and I need this code working
      // soon to heat my house. Don't judge me.
    }
  }

}
