package com.gregmarut.support.beangenerator.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.rule.RuleBuilder;
import com.gregmarut.support.beangenerator.rule.condition.FieldNameMatchesCondition;

public class CollidingRulesTest
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
		
		// Create 3 conflicting rules -- the last rule added should be used first
		ruleBuilder.forType(String.class).when(new FieldNameMatchesCondition("accountID")).thenReturn("12345");
		ruleBuilder.forType(String.class).when(new FieldNameMatchesCondition("accountID")).thenReturn("ABCDE");
		ruleBuilder.forType(String.class).when(new FieldNameMatchesCondition("accountID")).thenReturn("98765");
	}
	
	/**
	 * A test to assure that the rules defined above are executing correctly
	 */
	@Test
	public void ruleTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure the last rule added was the rule that was used
		assertEquals("98765", testBean.getAccountID());
		assertNotSame("ABCDE", testBean.getAccountID());
		assertNotSame("12345", testBean.getAccountID());
	}
}
