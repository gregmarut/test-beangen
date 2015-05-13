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
package com.gregmarut.support.beangenerator.rule.condition;

public abstract class FieldNameCondition implements Condition
{
	// holds the pattern of the field name to match
	protected final String pattern;
	
	/**
	 * Creates a new simple Rule to check the pattern against the field name
	 * 
	 * @param pattern
	 * @param value
	 */
	public FieldNameCondition(final String pattern)
	{
		// make sure the field name is not null
		if (null != pattern)
		{
			this.pattern = pattern;
		}
		else
		{
			throw new IllegalArgumentException("pattern cannot be null.");
		}
	}
}
