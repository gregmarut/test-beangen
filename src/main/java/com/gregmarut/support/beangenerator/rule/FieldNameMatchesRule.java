/*******************************************************************************
 * Copyright (c) 2013 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.support.beangenerator.rule;

public class FieldNameMatchesRule<V> extends FieldNameRule<V>
{
	public FieldNameMatchesRule(String pattern, V value)
	{
		super(pattern, value);
	}
	
	@Override
	public boolean isMatch(final Class<?> clazz, final String name)
	{
		// check to see if the field names match
		return pattern.equals(name);
	}
}
