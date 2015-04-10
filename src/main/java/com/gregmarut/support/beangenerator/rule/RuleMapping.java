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
package com.gregmarut.support.beangenerator.rule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class that contains a map of rules grouped by the type of parameter. Each rule is created and
 * grouped against one
 * specific type of parameter. These rules are used to handle specific cases for setter methods that
 * match the rule
 * pattern and parameter type. Whenever these methods are discovered during bean initialization,
 * their values are
 * substituted with the value declared in the rule.
 * 
 * @author Greg Marut
 */
public class RuleMapping
{
	// holds the map of rules for a specific type of class variable
	private final Map<Class<?>, List<Rule<?>>> ruleMap;
	
	/**
	 * Constructs a new RuleMapping object
	 */
	public RuleMapping()
	{
		// instantiate the rule map
		ruleMap = new HashMap<Class<?>, List<Rule<?>>>();
	}
	
	/**
	 * Returns the list of rules based on the class type
	 * 
	 * @param clazz
	 * @return List<MethodMatchingRule>
	 */
	public List<Rule<?>> get(final Class<?> clazz)
	{
		return ruleMap.get(clazz);
	}
	
	/**
	 * Adds a new rule
	 * 
	 * @param rule
	 */
	public void add(final Rule<?> rule)
	{
		// fetch the list from the map of rules
		List<Rule<?>> rules = get(rule.getReturnType());
		
		// check to see if the list is null
		if (null == rules)
		{
			// create a new list of rules
			rules = new LinkedList<Rule<?>>();
			
			// put this list of rules into the map
			ruleMap.put(rule.getReturnType(), rules);
		}
		
		// make sure the rule does not already exist
		if (!rules.contains(rule))
		{
			// add the rule to the list
			rules.add(rule);
		}
	}
	
	/**
	 * Determines if rules exist for a specific parameter type
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean contains(final Class<?> clazz)
	{
		return ruleMap.containsKey(clazz);
	}
	
	/**
	 * Removes a list of rules from the map based on the class
	 * 
	 * @param clazz
	 */
	public void remove(final Class<?> clazz)
	{
		ruleMap.remove(clazz);
	}
}
