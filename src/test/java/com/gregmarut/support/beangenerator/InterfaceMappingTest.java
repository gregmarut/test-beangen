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

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.config.InterfaceMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A class to demonstrate how to use and define custom interface mappings. During bean
 * initialization,
 * a new object is instantiated based on the type of object that is detected. Since interfaces
 * cannot
 * be directly instantiated, they must be mapped with another object to be used instead.
 * An example would be a java.util.List object. Since Lists are interfaces, an object such as
 * java.util.LinkedList must be used as the concrete object to instantiate instead.
 * 
 * @author Greg Marut
 */
public class InterfaceMappingTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
		
		// create a new interface mapper object
		InterfaceMapper interfaceMapper = new InterfaceMapper();
		
		// By default, java.util.List is already assigned to java.util.LinkedList. However, for this
		// example, we will change tell the mapper to use ArrayList instead of List
		interfaceMapper.put(List.class, ArrayList.class);
		
		// set the interface mapper in the BeanPropertyGenerator
		beanPropertyGenerator.getConfiguration().setInterfaceMapper(interfaceMapper);
	}
	
	/**
	 * A test to ensure that List interfaces are correctly instantiated using ArrayList objects
	 * instead
	 */
	@Test
	public void testListInterfaceMapping()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure that the list that was instantiated was actually an ArrayList because of what
		// was
		// defined in the InterfaceMapper object
		assertEquals(ArrayList.class, testBean.getFinalList().getClass());
	}
	
	/**
	 * A test to ensure that Map interfaces are correctly instantiated using HashMap objects instead
	 */
	@Test
	public void testMapInterfaceMapping()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure that the list that was instantiated was actually a HashMap because of what was
		// defined in the InterfaceMapper object
		// Map was already set in the InterfaceMapping object by default.
		assertEquals(HashMap.class, testBean.getMap().getClass());
	}
}
