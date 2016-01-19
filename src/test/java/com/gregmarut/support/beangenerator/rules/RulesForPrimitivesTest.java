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
package com.gregmarut.support.beangenerator.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.rule.RuleBuilder;
import com.gregmarut.support.beangenerator.rule.condition.FieldNameStartsWithCondition;

/**
 * This class demonstrates and tests that primitives are properly assigned with rules
 * 
 * @author Greg Marut
 */
public class RulesForPrimitivesTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
		
		// create a new rule builder
		RuleBuilder ruleBuilder = beanPropertyGenerator.getConfiguration().createRuleBuilder();
		
		// create a new rule to replace all primitive fields that start with "some"
		ruleBuilder.forType(Integer.class).when(new FieldNameStartsWithCondition("some")).thenReturn(5);
		ruleBuilder.forType(Float.class).when(new FieldNameStartsWithCondition("some")).thenReturn(3.14f);
		ruleBuilder.forType(Double.class).when(new FieldNameStartsWithCondition("some")).thenReturn(7.4562);
		ruleBuilder.forType(Short.class).when(new FieldNameStartsWithCondition("some")).thenReturn((short) 9);
	}
	
	@Test
	public void testPrimitives()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		AnotherTestBean anotherTestBean = testBean.getAnotherTestBean();
		
		// make sure that the string did not change
		assertSame("someID", anotherTestBean.getSomeID());
		
		// verify the primitive values were properly set
		assertEquals(5, anotherTestBean.getSomeNumber());
		assertEquals(3.14f, anotherTestBean.getSomeFloat(), 0f);
		assertEquals(7.4562, anotherTestBean.getSomeDouble(), 0f);
		assertEquals(9, anotherTestBean.getSomeShort());
	}
}
