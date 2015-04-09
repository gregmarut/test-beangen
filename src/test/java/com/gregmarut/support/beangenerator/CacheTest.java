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
		TestBean testBean1 = beanPropertyGenerator.get(TestBean.class);
		TestBean testBean2 = beanPropertyGenerator.get(TestBean.class);
		
		// create another object
		AnotherTestBean anotherTestBean = beanPropertyGenerator.get(AnotherTestBean.class);
		
		// these two beans should be exactly equal (even object references)
		Assert.assertEquals(testBean1, testBean2);
		Assert.assertTrue(testBean1 == testBean2);
		
		// these two beans should be exactly equal (even object references)
		Assert.assertEquals(anotherTestBean, testBean1.getAnotherTestBean());
		Assert.assertTrue(anotherTestBean == testBean1.getAnotherTestBean());
		
		// these two beans should be exactly equal (even object references)
		Assert.assertEquals(testBean1.getAnotherTestBean(), testBean2.getAnotherTestBean());
		Assert.assertTrue(testBean1.getAnotherTestBean() == testBean2.getAnotherTestBean());
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
		TestBean testBean1 = beanPropertyGenerator.get(TestBean.class);
		TestBean testBean2 = beanPropertyGenerator.get(TestBean.class);
		
		// create another object
		AnotherTestBean anotherTestBean = beanPropertyGenerator.get(AnotherTestBean.class);
		
		// these two beans should be different references
		Assert.assertNotSame(testBean1, testBean2);
		Assert.assertFalse(testBean1 == testBean2);
		
		// these two beans should be different references
		Assert.assertNotSame(anotherTestBean, testBean1.getAnotherTestBean());
		Assert.assertFalse(anotherTestBean == testBean1.getAnotherTestBean());
		
		// these two beans should be different references
		Assert.assertNotSame(testBean1.getAnotherTestBean(), testBean2.getAnotherTestBean());
		Assert.assertFalse(testBean1.getAnotherTestBean() == testBean2.getAnotherTestBean());
	}
}
