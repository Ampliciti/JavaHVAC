/*
 * Copyright (C) 2018-2020 jeffrey
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

import com.ampliciti.javahvac.config.ServerConfig;
import com.ampliciti.javahvac.dao.HVACDao;
import com.ampliciti.javahvac.dao.impl.SqliteHVACDao;
import com.ampliciti.javahvac.domain.CurrentNodeState;
import com.ampliciti.javahvac.rest.Routes;
import com.ampliciti.javahvac.rest.controllers.HealthCheckController;
import com.ampliciti.javahvac.rest.controllers.RegionOverrideController;
import com.ampliciti.javahvac.rest.controllers.SourceOverrideController;
import com.ampliciti.javahvac.rest.controllers.StatusController;
import com.ampliciti.javahvac.rest.controllers.ZoneController;
import com.ampliciti.javahvac.rules.Rule;
import com.ampliciti.javahvac.rules.RuleGenerator;
import com.ampliciti.javahvac.service.NodeService;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.restexpress.RestExpress;

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
   * Our managed rules.
   */
  private static ArrayList<Rule> managedRules;

  /**
   * Main method. Only one argument expected; a path to a properties file. Checks the arguments,
   * then calls the constructor for this class.
   *
   * @param args A single path to the properties file used for this app.
   */
  public static void main(String[] args) {
    String startupMessage = "Starting JavaHVAC Server! With path to YAML file of: ";
    boolean props = true;
    if (args == null || args.length != 1) {
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
    File yamlFile = new File(args[0]);
    if (!yamlFile.exists() || !yamlFile.isFile()) {
      String message =
          "YAML file at the path: " + args[0] + " does not exist. Application cannot start.";
      System.err.println(message);
      logger.error(message);
      System.exit(-1);
    }
    Main main = new Main(yamlFile);
    main.initApp();
  }

  /**
   * Constructor.
   *
   * @param yamlFile YAML file object for this app.
   */
  public Main(File yamlFile) {
    this.yamlFile = yamlFile;
  }

  /**
   * Does the actual work of starting up application.
   */
  public void initApp() {
    ServerConfig.buildConfig(yamlFile);
    logger.info("Configuration built for " + ServerConfig.getName());// let the user know they
                                                                     // picked the right config
    // setup our DB/DAO
    HVACDao dao;
    try {
      dao = new SqliteHVACDao(ServerConfig.getDbPath());
    } catch (SQLException e) {
      String message = "Could not connect to the database at path: " + ServerConfig.getDbPath()
          + "; application cannot start.";
      logger.fatal(message, e);
      logger.error(message);
      System.exit(-1);
    }
    // TODO: write config to DB (or maybe not?)

    NodeService ns = new NodeService();
    if (!ns.checkNodeConnections()) {
      logger.warn("Warning, not all nodes are acccessable. Proceeding without all running nodes.");
    } else {
      logger.info("We are able to connect to all nodes.");
    }
    // See what our nodes are up to right now...
    CurrentNodeState.refreshNodeState();
    // start up our worker threads
    startUpThreads();
    // Start up the REST API
    startRestAPI();

    logger.info("Program successfully started up!");
    // wait till it dies
    while (true) {
      try {
        Thread.sleep(500000);
      } catch (InterruptedException e) {
        logger.warn("Main thread interrupted; you probably don't care.", e);
      }
    }

  }

  /**
   * Starts up the worker threads.
   */
  private static void startUpThreads() {
    // Define Rules
    managedRules = RuleGenerator.generateManagedRules();
    // ArrayList<Rule> unmanagedRules = RuleGenerator.generateNonManagedZoneRules();

    // start up a thread to keep an eye on our nodes:
    String nodeWatcherThreadName = "NodeWatcherThread";
    Runnable nodeWatcher = new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep(5000);// check on the nodes every 5 seconds
          } catch (InterruptedException e) {

          }
          try {
            CurrentNodeState.refreshNodeState();
          } catch (Throwable t) {
            logger.error("Error refreshing nodes in " + nodeWatcherThreadName, t);
          }
        }
      }
    };

    Thread nodeWatcherThread = new Thread(nodeWatcher);
    nodeWatcherThread.setName(nodeWatcherThreadName);
    logger.info("Starting node watcher worker thread...");
    nodeWatcherThread.start();
    logger.info("Node watcher thread started.");

    // NOTE: System defaults to off for all zones; but cistern rules go into effect immedately
    // Start worker thread to see if any conditions need to be changed
    String ruleThreadName = "ManagedWorkerThread";
    Runnable managedWorker = new Runnable() {
      @Override
      public void run() {
        try {
          while (true) {

            for (Rule r : getManagedRules()) {// enforce all rules
              logger.info("Enforcing rule: " + r.getDefinition());
              if (r.enforceRule()) {
                logger.info("Done enforcing rule: " + r.getDefinition());
              } else {
                logger.error("Failed to enforce rule: " + r.getDefinition());
              }
            }
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              ;
            }

          }
        } catch (Throwable t) {
          logger.fatal("Error enforcing rules nodes in " + ruleThreadName, t);
        }
      }
    };

    Thread managedWorkerThread = new Thread(managedWorker);
    managedWorkerThread.setName(ruleThreadName);
    logger.info("Starting managed worker thread...");
    managedWorkerThread.start();
    logger.info("Managed worker thread started.");

    // // Start worker thread to see if any conditions need to be changed -- TODO, may not be needed
    // Runnable unmanagedWorker = new Runnable() {
    // @Override
    // public void run() {
    // for (Rule r : unmanagedRules) {// enforce all rules
    // r.enforceRule();
    // }
    // }
    // };
    //
    // Thread unmanagedWorkerThread = new Thread(unmanagedWorker);
    // logger.info("Starting unmanaged worker thread...");
    // unmanagedWorkerThread.start();
    // logger.info("Umanaged worker thread started.");

  }

  /**
   * Starts up the REST API.
   * 
   * Normally we'd not make this public, but it makes testing easier for now, and I'm too lazy to
   * use Reflection.
   */
  public void startRestAPI() {

    logger.info("Starting up REST API...");
    try {
      // RestExpress.setDefaultSerializationProvider(new SerializationProvider());
      logger.info("-----Attempting to start up JavaHVAC REST API-----");
      RestExpress server = new RestExpress().setName("Java HVAC")
          // .setBaseUrl(config.getBaseUrl())
          .setExecutorThreadCount(15).setMaxContentSize(512000);// half a meg

      Routes.define(new HealthCheckController(), new StatusController(), new ZoneController(),
          new SourceOverrideController(), new RegionOverrideController(), server);
      // Relationships.define(server);
      // configurePlugins(config, server);
      // mapExceptions(server);
      server.bind(8080);
      logger.info("-----JavaHVAC REST API initalized.-----");
      server.awaitShutdown();
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        @Override
        public void run() {
          logger.info("Shutting down JavaHVAC...");
        }
      }, "Shutdown-thread"));
    } catch (Exception e) {
      logger.error("Runtime exception when starting/running JavaHVAC. Could not start.", e);
      System.exit(-1);
    }
  }

  /**
   * Shuts down the REST API, mainly for testing purposes.
   */
  public void shutdownRestAPI() {

  }

  /**
   * Our managed rules.
   * 
   * @return the managedRules
   */
  public static ArrayList<Rule> getManagedRules() {
    return managedRules;
  }



}
