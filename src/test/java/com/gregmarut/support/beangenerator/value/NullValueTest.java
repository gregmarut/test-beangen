package com.gregmarut.support.beangenerator.value;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;

public class NullValueTest
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
		beanPropertyGenerator.getProperties().getDefaultValues().put(String.class, new NullValue<String>(String.class));
	}
	
	@Test
	public void simpleNullStringTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure all of the string fields were set correctly
		assertEquals(null, testBean.getAccountID());
		assertEquals(null, testBean.getFirstName());
		assertEquals(null, testBean.getLastName());
		assertEquals(null, testBean.getDateOfBirth());
		
		assertEquals(1, testBean.getIntData());
		assertEquals(1, testBean.getInteger().intValue());
		assertEquals(1f, testBean.getFloatData(), 0f);
	}
}
