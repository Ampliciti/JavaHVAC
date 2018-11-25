/*
 * Copyright (C) 2018 jeffrey
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

import java.io.File;
import org.apache.log4j.Logger;

/**
 * Main class. This is what starts the <b>Server Application</b> for JavaHVAC.
 *
 */
public class Main {

  /**
   * Logger for this class.
   */
  public static Logger logger = Logger.getLogger(Main.class);


  /**
   * File reference to the YAML file for this class.
   */
  private final File yamlFile;

  /**
   * Main method. Only one argument expected; a path to a properties file. Checks the arguments,
   * then calls the constructor for this class.
   *
   * @param args A single path to the properties file used for this app.
   */
  public static void main(String[] args) {
    String startupMessage = "Starting PiHomeSecurity Server! With path to YAML file of: ";
    boolean props = true;
    if (args.length != 1) {
      startupMessage += "None. Will not start! Please pass in a path as a command argument.";
      props = false;
    } else {
      startupMessage += args[0];
    }
    System.out.println(startupMessage);
    logger.info(startupMessage);
    if (!props) {
      System.exit(-1);
    }
    File propertiesFile = new File(args[0]);
    if (!propertiesFile.exists() || !propertiesFile.isFile()) {
      String message =
          "YAML file at the path: " + args[0] + " does not exist. Application cannot start.";
      System.err.println(message);
      logger.error(message);
      System.exit(-1);
    }
    Main main = new Main(propertiesFile);
    main.run();
  }


  /**
   * Constructor.
   *
   * @param yamlFile YAML file object for this app.
   */
  public Main(File yamlFile) {
    this.yamlFile = yamlFile;
  }

  public void run() {

    // TODO: Something more here
  }
}
