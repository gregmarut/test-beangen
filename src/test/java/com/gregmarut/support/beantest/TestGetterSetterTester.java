/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.support.beantest;

import org.junit.Before;
import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.ChildBean;
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
		getterSetterTester.execute(ChildBean.class);
	}
}
