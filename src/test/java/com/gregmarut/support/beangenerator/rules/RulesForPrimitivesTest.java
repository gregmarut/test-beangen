package com.gregmarut.support.beangenerator.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.AnotherTestBean;
import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.rule.FieldNameStartsWithRule;
import com.gregmarut.support.beangenerator.rule.Rule;
import com.gregmarut.support.beangenerator.rule.RuleMapping;

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
		
		// create a new rule mapping object
		RuleMapping ruleMapping = new RuleMapping();
		
		// create a new rule to replace all primitive fields that start with "some"
		Rule<Integer> substituteIntegerFieldsStartingWithSome = new FieldNameStartsWithRule<Integer>("some", 5);
		Rule<Float> substituteFloatFieldsStartingWithSome = new FieldNameStartsWithRule<Float>("some", 3.14f);
		Rule<Double> substituteDoubleFieldsStartingWithSome = new FieldNameStartsWithRule<Double>("some", 7.4562);
		Rule<Short> substituteShortFieldsStartingWithSome = new FieldNameStartsWithRule<Short>("some", (short) 9);
		
		// add this rule to the mapping object
		ruleMapping.add(substituteIntegerFieldsStartingWithSome);
		ruleMapping.add(substituteFloatFieldsStartingWithSome);
		ruleMapping.add(substituteDoubleFieldsStartingWithSome);
		ruleMapping.add(substituteShortFieldsStartingWithSome);
		
		// set this mapping object in the BeanPropertyGenerator
		beanPropertyGenerator.getProperties().setRuleMapping(ruleMapping);
	}
	
	@Test
	public void testPrimitives()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		AnotherTestBean anotherTestBean = testBean.getAnotherTestBean();
		
		//make sure that the string did not change
		assertSame("someID", anotherTestBean.getSomeID());
		
		//verify the primitive values were properly set
		assertEquals(5, anotherTestBean.getSomeNumber());
		assertEquals(3.14f, anotherTestBean.getSomeFloat(), 0f);
		assertEquals(7.4562, anotherTestBean.getSomeDouble(), 0f);
		assertEquals(9, anotherTestBean.getSomeShort());
	}
}
