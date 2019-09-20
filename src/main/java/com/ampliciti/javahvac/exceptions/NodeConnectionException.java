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
package com.ampliciti.javahvac.exceptions;

/**
 * An exception that indicates a problem communicating with a node.
 * 
 * @author jeffrey
 */
public class NodeConnectionException extends Exception {

  public NodeConnectionException() {
    super("Problem connecting to node.");
  }

  public NodeConnectionException(String message) {
    super("Problem connecting to node: " + message);
  }

  public NodeConnectionException(Exception e) {
    super("Problem connecting to node.", e);
  }

  public NodeConnectionException(String message, Exception e) {
    super("Problem connecting to node: " + message, e);
  }

  // there are better ways to do this, but I'm coding on my day off and in a hurry
  @Override
  public String getMessage() {
    if (super.getCause() != null) {
      return super.getMessage() + ": " + super.getCause().getMessage();
    } else {
      return super.getMessage();
    }
  }
}
