/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg Marut - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.support.beangenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;

public class InterfaceProxyTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator(false, true);
		
		// clear the interface mappings
		beanPropertyGenerator.getProperties().getInterfaceMapper().clear();
	}
	
	@Test
	public void testBean()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		boolean isEmpty = testBean.getMap().isEmpty();
		int size = testBean.getMap().size();
		
		// make sure the map is actually a proxy class
		assertTrue(Proxy.isProxyClass(testBean.getMap().getClass()));
		
		// make sure that the Map.isEmpty returns whatever the default value was set to for
		// "boolean"s in the BeanPropertyGenerator
		// default is set to true
		assertTrue(isEmpty);
		
		// make sure that the Map.size returns whatever the default value was set to for "int"s in
		// the BeanPropertyGenerator
		// default is set to 1
		assertEquals(1, size);
	}
}
