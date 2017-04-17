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

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListTest
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
	
	@Test
	public void simpleListTest()
	{
		// generate the list of test beans
		List<TestBean> testBeans = beanPropertyGenerator.getList(TestBean.class, 5);
		
		// make sure the size of the list is 5
		Assert.assertEquals(5, testBeans.size());
		
		// for each of the test beans
		for (TestBean testBean : testBeans)
		{
			// make sure all of the string fields were set correctly
			assertEquals("accountID", testBean.getAccountID());
			assertEquals("firstName", testBean.getFirstName());
			assertEquals("lastName", testBean.getLastName());
			assertEquals("dateOfBirth", testBean.getDateOfBirth());
		}
	}
	
	@Test
	public void interiorListTest()
	{
		// create a new test bean object
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure the lists is filled correctly
		Assert.assertEquals(BeanPropertyGenerator.DEFAULT_COLLECTION_AUTO_FILL_COUNT, testBean.getList().size());
		Assert.assertEquals(BeanPropertyGenerator.DEFAULT_COLLECTION_AUTO_FILL_COUNT, testBean.getFinalList().size());
		
		// for each of the objects
		for (int i = 0; i < BeanPropertyGenerator.DEFAULT_COLLECTION_AUTO_FILL_COUNT; i++)
		{
			// make sure there are values as expected
			assertAnotherTestBean(testBean.getList().get(i));
			assertAnotherTestBean(testBean.getFinalList().get(i));
		}
	}
	
	private void assertAnotherTestBean(final AnotherTestBean anotherTestBean)
	{
		Assert.assertEquals("something", anotherTestBean.getSomething());
		Assert.assertEquals("someID", anotherTestBean.getSomeID());
		Assert.assertEquals(1.0, anotherTestBean.getSomeDouble(), 0.0);
		Assert.assertEquals(1, anotherTestBean.getSomeNumber());
	}
}
