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
import com.gregmarut.support.beangenerator.rule.Rule;
import com.gregmarut.support.beangenerator.rule.RuleMapping;
import com.gregmarut.support.beangenerator.rule.condition.FieldNameMatchesCondition;
import com.gregmarut.support.beangenerator.rule.condition.FieldNameStartsWithCondition;

/**
 * This class demonstrates and tests when generics are not set for the rules
 * 
 * @author Greg Marut
 */
public class NoGenericTest
{
	// holds the BeanPropertyGenerator which is used for creating and populating objects with test
	// data
	private static BeanPropertyGenerator beanPropertyGenerator;
	
	@BeforeClass
	public static void setup()
	{
		// create a new BeanPropertyGenerator
		beanPropertyGenerator = new BeanPropertyGenerator();
		
		// create a new rule mapping object
		RuleMapping ruleMapping = new RuleMapping();
		
		// create a new rule to replace all primitive fields that start with "some"
		Rule<Integer> ruleWithNoGeneric = new Rule<Integer>(new FieldNameStartsWithCondition("some"), 5);
		Rule<Float> ruleWithNoGeneric2 = new Rule<Float>(new FieldNameStartsWithCondition("some"), 3.14f);
		Rule<Integer> ruleWithNoGeneric3 = new Rule<Integer>(new FieldNameMatchesCondition("integer"), 7);
		Rule<String> ruleWithNoGeneric4 = new Rule<String>(new FieldNameMatchesCondition("firstName"), "John");
		
		// add this rule to the mapping object
		ruleMapping.add(ruleWithNoGeneric);
		ruleMapping.add(ruleWithNoGeneric2);
		ruleMapping.add(ruleWithNoGeneric3);
		ruleMapping.add(ruleWithNoGeneric4);
		
		// set this mapping object in the BeanPropertyGenerator
		beanPropertyGenerator.getConfiguration().setRuleMapping(ruleMapping);
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
		
		// verify the integer
		assertEquals(new Integer(7), testBean.getInteger());
		
		// verify the strings
		assertEquals("John", testBean.getFirstName());
		
		// verify that these fields are not affected
		assertEquals("lastName", testBean.getLastName());
	}
}
