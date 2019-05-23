/*
 * Copyright (C) 2018-2019 jeffrey
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
import com.ampliciti.javahvac.rules.Rule;
import com.ampliciti.javahvac.rules.RuleGenerator;
import com.ampliciti.javahvac.service.NodeService;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
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
     * Main method. Only one argument expected; a path to a properties file.
     * Checks the arguments, then calls the constructor for this class.
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
            String message
                    = "YAML file at the path: " + args[0] + " does not exist. Application cannot start.";
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
        logger.info("Configuration built for " + ServerConfig.getName());//let the user know they picked the right config
        //setup our DB/DAO
        HVACDao dao;
        try {
            dao = new SqliteHVACDao(ServerConfig.getDbPath());
        } catch (SQLException e) {
            String message = "Could not connect to the database at path: " + ServerConfig.getDbPath() + "; application cannot start.";
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
        // Define Rules
        ArrayList<Rule> managedRules = RuleGenerator.generateManagedRules();
        ArrayList<Rule> unmanagedRules = RuleGenerator.generateNonManagedZoneRules();

        // NOTE: System defaults to off for all zones; but cistern rules go into effect immedately
        // TODO: Start REST API
        // Start worker thread to see if any conditions need to be changed
        Runnable managedWorker = new Runnable() {
            @Override
            public void run() {
                for (Rule r : managedRules) {//enforce all rules
                    r.enforceRule();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        };

        Thread managedWorkerThread = new Thread(managedWorker);
        logger.info("Starting managed worker thread...");
        managedWorkerThread.start();
        logger.info("Managed worker thread started.");

        // Start worker thread to see if any conditions need to be changed
        Runnable unmanagedWorker = new Runnable() {
            @Override
            public void run() {
                for (Rule r : unmanagedRules) {//enforce all rules
                    r.enforceRule();

                }
            }
        };

        Thread unmanagedWorkerThread = new Thread(unmanagedWorker);
        logger.info("Starting unmanaged worker thread...");
        unmanagedWorkerThread.start();
        logger.info("Umanaged worker thread started.");
        logger.info("Program successfully started up!");
        while (true) {//wait till it dies
            try {
                Thread.sleep(500000);
            } catch (Exception e) {

            }
        }

    }

}
