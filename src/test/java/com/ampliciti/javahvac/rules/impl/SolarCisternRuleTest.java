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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeffrey
 */
public class SolarCisternRuleTest {
    
    public SolarCisternRuleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDefinition method, of class SolarCisternRule.
     */
    @Test
    public void testGetDefinition() {
        System.out.println("getDefinition");
        SolarCisternRule instance = new SolarCisternRule("Cistern", 140);
        String expResult = "Solar cistern rule for: Cistern";
        String result = instance.getDefinition();
        assertEquals(expResult, result);
    }

    /**
     * Test of enforceRule method, of class SolarCisternRule.
     */
    @Test
    public void testEnforceRule() {
        System.out.println("enforceRule");
        SolarCisternRule instance = new SolarCisternRule("Cistern", 140);
        boolean expResult = true;
        boolean result = instance.enforceRule();
        assertEquals(expResult, result);
    }
    
}
