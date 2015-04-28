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
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gregmarut.support.bean.TestBean;
import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.rule.FieldNameMatchesRule;
import com.gregmarut.support.beangenerator.rule.Rule;
import com.gregmarut.support.beangenerator.rule.RuleMapping;

/**
 * This class demonstrates how to create rules for handling special cases.
 * 
 * @author Greg Marut
 */
public class FieldNameMatchesRuleTest
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
		
		// create a new rule to replace the accountID fields.
		// Therefore, for these specific cases, we want the accountID fields to
		// be set to a number "12345". This rule tells the BeanPropertyGenerator to handle these
		// cases specially. Otherwise, the code that is assuming that these string fields contain
		// numbers and will fail.
		Rule<String> substituteStringAccountIDField = new FieldNameMatchesRule<String>("accountID", "12345");
		
		// create a new matching rule to set the date fields to dates instead of the method name.
		// The code is expecting these fields to contain date strings that can be parsed into date
		// objects
		Rule<String> substituteStringDateOfBirthField = new FieldNameMatchesRule<String>("dateOfBirth", "01/01/2011");
		
		// add this rule to the mapping object
		ruleMapping.add(substituteStringAccountIDField);
		ruleMapping.add(substituteStringDateOfBirthField);
		
		// set this mapping object in the BeanPropertyGenerator
		beanPropertyGenerator.getProperties().setRuleMapping(ruleMapping);
	}
	
	/**
	 * A test to assure that methods that match defined rules are properly handled as expected based
	 * on the above rules.
	 */
	@Test
	public void ruleTest()
	{
		// create a new test object and fill every field with test data
		TestBean testBean = beanPropertyGenerator.get(TestBean.class);
		
		// make sure that the string fields that did not match any of the above rules were
		// unaffected
		assertEquals("firstName", testBean.getFirstName());
		assertEquals("lastName", testBean.getLastName());
		
		// the accountID and dateOfBirth fields have been set as special rules and should no
		// longer follow the default pattern for setting fields
		// make sure the defaults are no longer set for these fields
		assertNotSame("accountID", testBean.getAccountID());
		assertNotSame("dateOfBirth", testBean.getDateOfBirth());
		
		// Because of the rules stated above, the new values should be set to as defined in the
		// rules
		// The accountID field should now be set to "12345"
		// The dateOfBirth field should now be set to "01/01/2011"
		assertEquals("12345", testBean.getAccountID());
		assertEquals("01/01/2011", testBean.getDateOfBirth());
		
		// now we can parse the strings as integers since these values have been changed due to the
		// rules
		int accountID = Integer.parseInt(testBean.getAccountID());
		
		// make sure the integers were able to be parsed correctly from the ID fields
		assertEquals(12345, accountID);
		
		try
		{
			// attempt to parse the date from this string
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.parse(testBean.getDateOfBirth());
		}
		catch (ParseException e)
		{
			// fail the test because the date was not able to be parsed from the string
			fail("Unable to parse date: " + testBean.getDateOfBirth());
		}
	}
}
