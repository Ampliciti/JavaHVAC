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
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.dao.HVACDao;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

/**
 * Dao specifically for SQLite. Using Hibernate, mainly because I'm in a hurry.
 *
 * @author jeffrey
 */
public class SqliteHVACDao implements HVACDao {

    /**
     * SQLite URL prefix.
     */
    private static final String URL_PREFIX = "jdbc:sqlite:";

    /**
     * URL for the database.
     */
    private final String url;

    /**
     * Logger for this class.
     */
    public static Logger logger = Logger.getLogger(SqliteHVACDao.class);

//    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    /**
     * Constructor. Checks that it can create a db connection as part of the
     * setup.
     *
     * @param path Path or connection string to use.
     * @throws SQLException if there's a problem
     */
    public SqliteHVACDao(String path) throws SQLException {
        url = URL_PREFIX + path;
        try (Connection c = createConnection()) {
            if (c != null) {
                DatabaseMetaData meta = c.getMetaData();
                logger.info("Connected to SQLite DB with path:" + meta.getURL());
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                registryBuilder.applySettings(establishSettings(url));
                StandardServiceRegistry registry = registryBuilder.build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
                sessionFactory.openSession().close();//open a session and close it, just as a test (probably not needed, but just in case)                
            }
        } catch (SQLException e) {
            logger.error("Could not create/connect to DB at path: " + path, e);
            throw e;
        }
    }

    private static Map<String, String> establishSettings(String url) {
        // Hibernate settings equivalent to hibernate.cfg.xml's properties
        Map<String, String> settings = new HashMap<>();
        settings.put(Environment.DRIVER, "org.sqlite.JDBC");
        settings.put(Environment.URL, url);
//            settings.put(Environment.USER, "postgres");
//            settings.put(Environment.PASS, "admin");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.SQLiteDialect");
        return settings;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /**
     * Safely closes a connection.
     *
     * @param c Connection to close. Can be null.
     */
    private void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                //i don't care
                logger.trace("Cannot close connection.", ex);
            }
        }
    }

    /**
     * Gets the full URL/Path to the SQLite DB
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

}
