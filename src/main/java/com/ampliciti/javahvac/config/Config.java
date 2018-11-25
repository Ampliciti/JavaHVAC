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
package com.ampliciti.javahvac.config;

import com.ampliciti.javahvac.domain.Region;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Config object for our application. All accessors are static, so once built, this object does not
 * need to be passed around.
 *
 * @author jeffrey
 */
public class Config {

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


  // builder stuff
  /**
   * Builds this object out, statically.
   *
   * @param yamlFile Yaml config to use to build.
   * @throws IllegalArgumentException if the YAML is missing, unreadable, or malformed.
   */
  public static void buildConfig(File yamlFile) throws IllegalArgumentException {
    loadYaml(yamlFile);
  }

  /**
   * Loads a yaml file object into a Yaml object.
   *
   * @param yamlFile
   * @return A map representing the Yaml file.
   */
  private static Config loadYaml(File yamlFile) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(yamlFile);
      return mapper.readValue(fis, Config.class);
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
