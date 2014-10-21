package com.gregmarut.support.beantest;

import org.junit.Before;
import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;

public class TestGetterSetterTester
{
	private GetterSetterTester getterSetterTester;
	
	@Before
	public void setUp()
	{
		getterSetterTester = new GetterSetterTester();
	}
	
	@Test
	public void testBeans()
	{
		getterSetterTester.execute(TestBean.class);
		getterSetterTester.execute(AnotherTestBean.class);
	}
}
