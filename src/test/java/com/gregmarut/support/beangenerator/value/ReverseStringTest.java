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
package com.gregmarut.support.beangenerator.value;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;

public class ReverseStringTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
		
		// override the string functionality to use reversed strings
		beanPropertyGenerator.getConfiguration().getDefaultValues().put(String.class, new StringValue()
		{
			@Override
			public String getValue(Field field)
			{
				return new StringBuilder(super.getValue(field)).reverse().toString();
			}
		});
	}
	
	@Test
	public void simpleStringTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure all of the string fields were set correctly
		assertEquals("DItnuocca", testBean.getAccountID());
		assertEquals("emaNtsrif", testBean.getFirstName());
		assertEquals("emaNtsal", testBean.getLastName());
		assertEquals("htriBfOetad", testBean.getDateOfBirth());
	}
}
