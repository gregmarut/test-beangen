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

import com.gregmarut.support.beangenerator.ClassConversionUtil;

/**
 * Contains the base class for determining a Rule
 * 
 * @author Greg Marut
 */
public abstract class Rule<V>
{
	// holds the value for this rule
	private final V value;
	
	public Rule(final V value)
	{
		this.value = value;
	}
	
	/**
	 * Holds the return type of the value for this rule
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<V> getReturnType()
	{
		return (Class<V>) ClassConversionUtil.convertToNonPrimitive(value.getClass());
	}
	
	/**
	 * Returns the value to return when this rule is matched
	 * 
	 * @return
	 */
	public V getValue()
	{
		return value;
	}
	
	/**
	 * Determines if this rule matches the given class type and name
	 * 
	 * @param clazz
	 *        The type of value that is being checked
	 * @param name
	 *        The name of the attribute
	 * @return
	 */
	public abstract boolean isMatch(final Class<?> clazz, final String name);
}
