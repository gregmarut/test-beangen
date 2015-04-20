/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.support.beangenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.value.StaticValue;

/**
 * This class demonstrates how to use the BeanPropertyGenerator while changing the default values
 * 
 * @author Greg Marut
 */
public class DefaultValuesTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
		
		// create a new default values object
		DefaultValues defaultValues = new DefaultValues();
		
		// change the default "Integer" (Object) and "int" (Primitive) to the number 42
		defaultValues.put(Integer.class, 42);
		defaultValues.put(int.class, 42);
		
		// change the default value of AnotherTestObject to be set to null
		defaultValues.put(AnotherTestBean.class, new StaticValue<AnotherTestBean>(null, AnotherTestBean.class));
		
		// set the default values object into the bean property generator
		beanPropertyGenerator.getProperties().setDefaultValues(defaultValues);
	}
	
	/**
	 * Test to assure that the default value for "int"s and "Integer"s have been changed
	 */
	@Test
	public void changedIntegerValues()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure the values are no longer the default
		assertNotSame(DefaultValues.DEFAULT_INTEGER, testBean.getInteger());
		assertNotSame(DefaultValues.DEFAULT_INTEGER, testBean.getIntData());
		
		// make sure the values were successfully changed to 42
		assertEquals(new Integer(42), testBean.getInteger());
		assertEquals(42, testBean.getIntData());
	}
	
	/**
	 * Test to assure the class {@link AnotherTestBean} was changed to null
	 */
	@Test
	public void changedAnotherTestBeanToNull()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure the "AnothertestBean" is null since it was changed in the DefaultValues object
		assertNull(testBean.getAnotherTestBean());
	}
}
