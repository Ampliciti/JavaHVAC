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
package com.ampliciti.javahvac.domain.config;

/**
 * Nodes are actors and/or sensors that talk to the server to send data and take actions. Nodes
 * themselves are not associated with zones, however, node functions are. The purpose of having the
 * node list here is so that the server knows what nodes to expect to connect to, and can send out
 * warnings if they don't exist. Nodes must have DNS names or IP addresses that resolve on the local
 * network, for that reason, if using IPs, static DHCP is recommended.
 *
 * @author jeffrey
 */
public class Node {


  /**
   * Name for this node.
   */
  private String name;

  /**
   * Address for this node.
   */
  private String address;

  /**
   * Constructor.
   * 
   * @param name Name for this node.
   * @param address Address for this node.
   */
  public Node(String name, String address) {
    this.name = name;
    this.address = address;
  }



  /**
   * Default constructor.
   */
  public Node() {}

  /**
   * Get the address for this node.
   * 
   * @return
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set the address for this node.
   * 
   * @param address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Node{" + "name=" + name + ", address=" + address + '}';
  }



}
