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
package com.ampliciti.javahvac.config;

import com.ampliciti.javahvac.domain.Node;
import com.ampliciti.javahvac.domain.Region;
import com.ampliciti.javahvac.domain.Source;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * Config object for our application. All accessors are static, so once built, this object does not
 * need to be passed around.
 *
 * @author jeffrey
 */
public class ServerConfig {


  /**
   * Logger for this class.
   */
  private static Logger logger = Logger.getLogger(ServerConfig.class);

  /**
   * The name of your building complex and/or setup. This is used for logging and UI purposes.
   * Example: "Smith Household" or "ABC Company".
   */
  private static String name;

  /**
   * Locally resolveable DNS or IP address.
   */
  private static String dns;

  /**
   * A region is an area serviced by a single pump/blower/etc.
   */
  private static ArrayList<Region> regions;

  /**
   * Sources of heat and cool; can be for more than one region; region names must match regions.
   */
  private static ArrayList<Source> sources;

  /**
   * Nodes that the server should be expecting to communicate with.
   */
  private static ArrayList<Node> nodes;

  /**
   * Email address that critical notifications will be sent.
   */
  private static ArrayList<String> notificationEmail;

  /**
   * Path where the database will be stored.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private static String dbPath = "./hvac-sqlite.db";

  /**
   * The name of your building complex and/or setup. This is used for logging and UI purposes.
   * Example: "Smith Household" or "ABC Company".
   *
   * @return the name
   */
  public static String getName() {
    return name;
  }

  /**
   * Locally resolveable DNS or IP address.
   *
   * @return the dns
   */
  public static String getDns() {
    return dns;
  }

  /**
   * A region is an area serviced by a single pump/blower/etc.
   *
   * @return the regions
   */
  public static ArrayList<Region> getRegions() {
    return regions;
  }

  /**
   * Sources of heat and cool; can be for more than one region; region names must match regions.
   *
   * @return the sources
   */
  public static ArrayList<Source> getSources() {
    return sources;
  }

  /**
   * Path where the database will be stored.
   *
   * @return the dbPath
   */
  public static String getDbPath() {
    return dbPath;
  }

  /**
   * Path where the database will be stored.
   *
   * @param aDbPath the dbPath to set
   */
  public static void setDbPath(String aDbPath) {
    dbPath = aDbPath;
  }

  /**
   * The name of your building complex and/or setup. This is used for logging and UI purposes.
   * Example: "Smith Household" or "ABC Company".
   *
   * @param aName the name to set
   */
  public void setName(String aName) {
    name = aName;
  }

  /**
   * Locally resolveable DNS or IP address.
   *
   * @param dns the dns to set
   */
  public void setDns(String dns) {
    this.dns = dns;
  }

  /**
   * A region is an area serviced by a single pump/blower/etc.
   *
   * @param regions the regions to set
   */
  public void setRegions(ArrayList<Region> regions) {
    this.regions = regions;
  }

  /**
   * Sources of heat and cool; can be for more than one region; region names must match regions.
   *
   * @param aSources the sources to set
   */
  public void setSources(ArrayList<Source> aSources) {
    sources = aSources;
  }

  /**
   * Nodes that the server should be expecting to communicate with.
   *
   * @return the nodes
   */
  public static ArrayList<Node> getNodes() {
    return nodes;
  }

  /**
   * Nodes that the server should be expecting to communicate with.
   *
   * @param aNodes the nodes to set
   */
  public void setNodes(ArrayList<Node> aNodes) {
    nodes = aNodes;
  }

  /**
   * Email address that critical notifications will be sent.
   *
   * @return the notificationEmail
   */
  public static ArrayList<String> getNotificationEmail() {
    return notificationEmail;
  }

  /**
   * Email address that critical notifications will be sent.
   *
   * @param aNotificationEmail the notificationEmail to set
   */
  public void setNotificationEmail(ArrayList<String> aNotificationEmail) {
    notificationEmail = aNotificationEmail;
  }

  // builder stuff
  /**
   * Builds this object out, statically.
   *
   * @param yamlFile Yaml config to use to build.
   * @throws IllegalArgumentException if the YAML is missing, unreadable, or malformed.
   */
  public static void buildConfig(File yamlFile) throws IllegalArgumentException {
    loadYaml(yamlFile);
    // check to make sure we have a source for each region
    for (Region r : regions) {
      boolean nameMatch = false;
      for (Source s : sources) {
        for (String regionServedName : s.getRegions_served()) {
          if (regionServedName.equals(r.getName())) {
            nameMatch = true;
          }
        }
      }
      if (!nameMatch) {
        logger.warn("Region: '" + r.getName() + "' does not have a heat/cool source.");
      }
    }
  }

  /**
   * Loads a yaml file object into a Yaml object.
   *
   * @param yamlFile
   * @return A map representing the Yaml file.
   */
  private static ServerConfig loadYaml(File yamlFile) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(yamlFile);
      return mapper.readValue(fis, ServerConfig.class);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
          ;// nothing we can do
        }
      }
    }
  }
}
