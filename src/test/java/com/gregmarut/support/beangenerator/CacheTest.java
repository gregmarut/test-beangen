package com.gregmarut.support.beangenerator;

import junit.framework.Assert;

import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;

/**
 * This test demonstrates that the cache is working correctly
 * 
 * @author Greg Marut
 */
public class CacheTest
{
	/**
	 * A test to ensure that when caching is enabled, the cache is properly checked for existing classes
	 */
	@Test
	public void cacheTest()
	{
		// create a new BeanPropertyGenerator
		BeanPropertyGenerator beanPropertyGenerator = new BeanPropertyGenerator(true);
		
		// create a new test object
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// create another object
		AnotherTestBean anotherTestBean = beanPropertyGenerator.get(AnotherTestBean.class);
		
		// these two beans should be exactly equal (even object references)
		Assert.assertEquals(anotherTestBean, testBean.getAnotherTestBean());
		Assert.assertTrue(anotherTestBean == testBean.getAnotherTestBean());
	}
	
	/**
	 * A test to ensure that when caching is disabled, the cache is not checked
	 */
	@Test
	public void noCacheTest()
	{
		// create a new BeanPropertyGenerator
		BeanPropertyGenerator beanPropertyGenerator = new BeanPropertyGenerator(false);
		
		// create a new test object
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// create another object
		AnotherTestBean anotherTestBean = beanPropertyGenerator.get(AnotherTestBean.class);
		
		// these two beans should be exactly equal (even object references)
		Assert.assertNotSame(anotherTestBean, testBean.getAnotherTestBean());
		Assert.assertFalse(anotherTestBean == testBean.getAnotherTestBean());
	}
}
