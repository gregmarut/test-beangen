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

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.config.DefaultValues;

/**
 * This class demonstrates the simple uses of BeanPropertyGenerator
 * 
 * @author Greg Marut
 */
public class SimpleTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
	}
	
	/**
	 * A simple test to assure that the String fields in the object were set to the name of the method (minus the "set")
	 * Example - "setAccountID" gets set to "AccountID"
	 */
	@Test
	public void simpleStringTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure all of the string fields were set correctly
		assertEquals("accountID", testBean.getAccountID());
		assertEquals("firstName", testBean.getFirstName());
		assertEquals("lastName", testBean.getLastName());
		assertEquals("dateOfBirth", testBean.getDateOfBirth());
	}
	
	/**
	 * A simple test to assure that values that have been specified in the {@link DefaultValues} have been set as
	 * expected
	 */
	@Test
	public void simpleDefaultValuesTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure all the fields that had default values specified were set correctly
		assertEquals(DefaultValues.DEFAULT_FLOAT, (Float) testBean.getFloatData());
		assertEquals(DefaultValues.DEFAULT_INTEGER, (Integer) testBean.getIntData());
		assertEquals(DefaultValues.DEFAULT_INTEGER, testBean.getInteger());
		
		assertEquals(1, testBean.getAnotherTestBean().getSomeNumbers().length);
		assertEquals(DefaultValues.DEFAULT_INTEGER.intValue(), testBean.getAnotherTestBean().getSomeNumbers()[0]);
		
		assertEquals(1, testBean.getAnotherTestBean().getSomeBytes().length);
		assertEquals(DefaultValues.DEFAULT_BYTE.byteValue(), testBean.getAnotherTestBean().getSomeBytes()[0]);
	}
}
