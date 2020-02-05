package com.gregmarut.support.beangenerator;

import com.gregmarut.support.bean.PrivateConstructorBean;
import com.gregmarut.support.bean.ProtectedConstructorBean;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConstructorTest
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
	public void constructPrivateConstructor()
	{
		PrivateConstructorBean privateConstructorBean = beanPropertyGenerator.get(PrivateConstructorBean.class);
		Assert.assertEquals("param1", privateConstructorBean.getParam1());
	}
	
	@Test
	public void constructProtectedConstructor()
	{
		ProtectedConstructorBean protectedConstructorBean = beanPropertyGenerator.get(ProtectedConstructorBean.class);
		Assert.assertEquals("param1", protectedConstructorBean.getParam1());
	}
}
