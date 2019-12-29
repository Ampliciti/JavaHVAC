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
package com.ampliciti.javahvac.domain;

/**
 * Class that's used to post misc notices on /info api. This is used for informational messages
 * about the current system state. All it's accessors are static, so really any object can modify
 * it. This shouldn't be used for business logic, rule enforcement, or flow control.
 * 
 * @author jeffrey
 */
public class MiscNotices {

  /**
   * Any notice about the cistern.
   */
  private static String cisternNotice = null;



  /**
   * Any notice about the cistern.
   * 
   * @return the cisternNotice
   */
  public static String getCisternNotice() {
    return cisternNotice;
  }

  /**
   * Any notice about the cistern.
   * 
   * @param aCisternNotice the cisternNotice to set
   */
  public static void setCisternNotice(String aCisternNotice) {
    cisternNotice = aCisternNotice;
  }


}
