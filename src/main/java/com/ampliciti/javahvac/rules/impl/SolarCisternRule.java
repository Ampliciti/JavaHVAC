/*
 * Copyright (C) 2019 jeffrey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ampliciti.javahvac.rules.impl;

import com.ampliciti.javahvac.rules.Rule;

/**
 * Rule for maintaining a solar cistern.
 *
 * @author jeffrey
 */
public class SolarCisternRule implements Rule {

    /**
     * Maximum temp that we'll allow our cistern to get to.
     */
    private float maxTemp;

    /**
     * Name for this solar cistern.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param name Name for this solar cistern.
     * @param maxTemp Maximum temp that we'll allow our cistern to get to.
     */
    public SolarCisternRule(String name, float maxTemp) {
        this.maxTemp = maxTemp;
        this.name = name;
    }

    @Override
    public String getDefinition() {
        return "Solar cistern rule for: " + name;
    }

    @Override
    public boolean enforceRule() {
        int currentTemp = 0;//TODO; implement this!
        if (currentTemp > maxTemp) {
            //TODO: Turn off pump
        } else {
            //TODO: Turn on pump
        }
        return true;
    }

}
