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
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.rule.RuleBuilder;
import com.gregmarut.support.beangenerator.rule.condition.AndCondition;
import com.gregmarut.support.beangenerator.rule.condition.Condition;
import com.gregmarut.support.beangenerator.rule.condition.DeclaringClassCondition;
import com.gregmarut.support.beangenerator.rule.condition.FieldNameEndsWithCondition;

public class AndRulesTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private BeanPropertyGenerator beanPropertyGenerator;
	
	@Before
	public void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
		
		// create a new rule builder
		RuleBuilder ruleBuilder = beanPropertyGenerator.getConfiguration().createRuleBuilder();
		
		// create the conditions for 2 different class types
		Condition idOnTestBeanCondition = new AndCondition(new DeclaringClassCondition(TestBean.class),
				new FieldNameEndsWithCondition("ID"));
		Condition idOnAnotherTestBeanCondition = new AndCondition(new DeclaringClassCondition(AnotherTestBean.class),
				new FieldNameEndsWithCondition("ID"));
		
		// Create the 2 rules
		ruleBuilder.forType(String.class).when(idOnTestBeanCondition).thenReturn("testBeanID");
		ruleBuilder.forType(String.class).when(idOnAnotherTestBeanCondition).thenReturn("anotherTestBeanID");
	}
	
	/**
	 * A test to assure that the rules defined above are executing correctly
	 */
	@Test
	public void ruleTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// verify the results are as expected
		assertEquals("testBeanID", testBean.getAccountID());
		assertNotSame("anotherTestBeanID", testBean.getAccountID());
		
		assertEquals("anotherTestBeanID", testBean.getAnotherTestBean().getSomeID());
		assertNotSame("testBeanID", testBean.getAnotherTestBean().getSomeID());
	}
}
