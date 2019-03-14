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
package com.ampliciti.javahvac.domain;

import java.util.ArrayList;

/**
 * A region is an area serviced by a single pump/blower/etc.
 *
 * @author jeffrey
 */
public class Region {

    /**
     * Name of this region. Examples: "Region 1", "First Floor", "House".
     */
    private String name;

    /**
     * Flag indicating if this region should be computer logic manged or
     * manually operated only. Defaults to true (computer managed).
     */
    private boolean managed = true;

    /**
     * Zones associated with this region.
     */
    private ArrayList<Zone> zones;

    /**
     * Default constructor.
     */
    public Region() {
    }

    /**
     * Name of this region. Examples: "Region 1", "First Floor", "House".
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Zones associated with this region.
     *
     * @return the zones
     */
    public ArrayList<Zone> getZones() {
        return zones;
    }

    /**
     * Zones associated with this region. Warning: this will override any zones
     * you have already set with addZone();
     *
     * @param zones the zones to set
     */
    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }

    /**
     * Appends a zone to the zone list.
     *
     * @param zone the zones to append to the zone list.
     */
    public void addZone(Zone zone) {
        if (this.zones == null) {
            this.zones = new ArrayList<>(1);
        }
        zones.add(zone);
    }

    /**
     * Name of this region. Examples: "Region 1", "First Floor", "House".
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Flag indicating if this region should be computer logic managed or
     * manually operated only. Defaults to true (computer managed).
     *
     * @return True if this region should be computer managed, false if it is
     * manually managed.
     */
    public boolean isManaged() {
        return managed;
    }

}
