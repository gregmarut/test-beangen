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
package com.gregmarut.support.beangenerator.rule;

import java.lang.reflect.Field;

import com.gregmarut.support.beangenerator.rule.condition.Condition;
import com.gregmarut.support.beangenerator.value.StaticValue;
import com.gregmarut.support.beangenerator.value.Value;
import com.gregmarut.support.util.ClassConversionUtil;

/**
 * Contains the base class for determining a Rule
 * 
 * @author Greg Marut
 */
public class Rule<V> implements Condition
{
	// holds the condition for this rule
	private final Condition condition;
	
	// holds the value for this rule
	private final Value<V> value;
	
	@SuppressWarnings("unchecked")
	public Rule(final Condition condition, final V value)
	{
		// make sure the condition is not null
		if (null == condition)
		{
			throw new IllegalArgumentException("condition cannot be null");
		}
		
		// make sure the value is not null
		if (null == value)
		{
			throw new IllegalArgumentException("value cannot be null");
		}
		
		this.condition = condition;
		this.value = new StaticValue<V>(value, (Class<V>) value.getClass());
	}
	
	public Rule(final Condition condition, final Value<V> value)
	{
		// make sure the condition is not null
		if (null == condition)
		{
			throw new IllegalArgumentException("condition cannot be null");
		}
		
		// make sure the value is not null
		if (null == value)
		{
			throw new IllegalArgumentException("value cannot be null");
		}
		
		this.condition = condition;
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
		return (Class<V>) ClassConversionUtil.convertToNonPrimitive(value.getType());
	}
	
	/**
	 * Returns the value to return when this rule is matched
	 * 
	 * @return
	 */
	public Value<V> getValue()
	{
		return value;
	}
	
	@Override
	public boolean isTrue(Class<?> clazz, Field field)
	{
		return condition.isTrue(clazz, field);
	}
}
