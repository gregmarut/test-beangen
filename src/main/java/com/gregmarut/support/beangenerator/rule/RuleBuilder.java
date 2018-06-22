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

import com.gregmarut.support.beangenerator.rule.condition.AndCondition;
import com.gregmarut.support.beangenerator.rule.condition.Condition;
import com.gregmarut.support.beangenerator.rule.condition.OrCondition;
import com.gregmarut.support.beangenerator.value.StaticValue;
import com.gregmarut.support.beangenerator.value.Value;

public class RuleBuilder
{
	// holds the ruleMapping object for this rule builder to add rules to once they are built
	private final RuleMapping ruleMapping;
	
	public RuleBuilder(final RuleMapping ruleMapping)
	{
		this.ruleMapping = ruleMapping;
	}
	
	public <T> RBType<T> forType(final Class<T> clazz)
	{
		return new RBType<T>(clazz);
	}
	
	public class RBType<T>
	{
		private final Class<T> clazz;
		
		public RBType(final Class<T> clazz)
		{
			this.clazz = clazz;
		}
		
		/**
		 * Set the condition to use for this rule
		 * 
		 * @param condition
		 * @return
		 */
		public RBWhen<T> when(final Condition condition)
		{
			return new RBWhen<T>(this, condition);
		}
	}
	
	public class RBWhen<T>
	{
		private final RBType<T> rbType;
		private Condition condition;
		
		public RBWhen(final RBType<T> rbType, final Condition condition)
		{
			// make sure the condition is not null
			if (null == condition)
			{
				throw new IllegalArgumentException("condition cannot be null");
			}
			
			this.rbType = rbType;
			this.condition = condition;
		}
		
		public RBWhen<T> and(final Condition condition)
		{
			// wrap the existing condition and this condition with an "and" condition
			this.condition = new AndCondition(this.condition, condition);
			return this;
		}
		
		public RBWhen<T> or(final Condition condition)
		{
			// wrap the existing condition and this condition with an "or" condition
			this.condition = new OrCondition(this.condition, condition);
			return this;
		}
		
		/**
		 * Set the value to be used when the condition evaluates to true
		 * 
		 * @param value
		 */
		public void thenReturn(final T value)
		{
			thenReturn(new StaticValue<T>(value, rbType.clazz));
		}
		
		/**
		 * Set the value to be used when the condition evaluates to true
		 * 
		 * @param value
		 */
		public void thenReturn(final Value<T> value)
		{
			// make sure the value is not null
			if (null == value)
			{
				throw new IllegalArgumentException("value cannot be null");
			}
			
			// build the rule
			Rule<T> rule = new Rule<T>(condition, value);
			
			// add this rule to the configuration
			ruleMapping.add(rule);
		}
	}
}
