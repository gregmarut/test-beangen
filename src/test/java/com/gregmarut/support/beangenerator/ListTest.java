package com.gregmarut.support.beangenerator;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;

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
}
