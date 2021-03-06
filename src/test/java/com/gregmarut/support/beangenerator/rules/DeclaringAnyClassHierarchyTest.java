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

import org.junit.Before;
import org.junit.Test;

import com.gregmarut.support.bean.ChildBean;
import com.gregmarut.support.bean.ParentBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.rule.RuleBuilder;
import com.gregmarut.support.beangenerator.rule.condition.AndCondition;
import com.gregmarut.support.beangenerator.rule.condition.Condition;
import com.gregmarut.support.beangenerator.rule.condition.DeclaringAnyClassCondition;
import com.gregmarut.support.beangenerator.rule.condition.FieldNameMatchesCondition;

public class DeclaringAnyClassHierarchyTest
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
		Condition fieldNameMatchesOnChildBean = new AndCondition(new DeclaringAnyClassCondition(ChildBean.class),
			new FieldNameMatchesCondition("grandparentString"));
		Condition fieldNameMatchesOnParentBean = new AndCondition(new DeclaringAnyClassCondition(ParentBean.class),
			new FieldNameMatchesCondition("grandparentString"));
		
		// Create the 2 rules
		ruleBuilder.forType(String.class).when(fieldNameMatchesOnChildBean).thenReturn("child");
		ruleBuilder.forType(String.class).when(fieldNameMatchesOnParentBean).thenReturn("parent");
	}
	
	/**
	 * A test to assure that the rules defined above are executing correctly
	 */
	@Test
	public void ruleTest()
	{
		// create a new test object and fill every field with test data
		ChildBean childBean = beanPropertyGenerator.get(ChildBean.class);
		ParentBean parentBean = beanPropertyGenerator.get(ParentBean.class);
		
		// verify the results are as expected
		assertEquals("child", childBean.getGrandparentString());
		assertEquals("parent", parentBean.getGrandparentString());
	}
}
