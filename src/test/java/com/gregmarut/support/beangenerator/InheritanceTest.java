package com.gregmarut.support.beangenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.ChildBean;

public class InheritanceTest
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
	 * A test to ensure that the fields in the parent classes are set properly
	 */
	@Test
	public void simpleInheritanceTest()
	{
		// create a new test object and fill every field with test data
		ChildBean childBean = beanPropertyGenerator.get(ChildBean.class);
		
		// make sure the parent strings are not null
		assertNotNull(childBean.getSomeParentString());
		assertNotNull(childBean.getAnotherParentString());
		assertNotNull(childBean.getGrandparentString());
		
		// make sure all of the string fields were set correctly
		assertEquals("someParentString", childBean.getSomeParentString());
		assertEquals("anotherParentString", childBean.getAnotherParentString());
		assertEquals("grandparentString", childBean.getGrandparentString());
		assertEquals(1, childBean.getChildValue());
	}
}
