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

public class FieldNameStartsWithRule<V> extends FieldNameRule<V>
{
	public FieldNameStartsWithRule(final String pattern, final V value)
	{
		super(pattern, value);
	}
	
	@Override
	public boolean isMatch(final Class<?> clazz, final String name)
	{
		// make sure the name is not null
		if (null != name)
		{
			return name.startsWith(pattern);
		}
		else
		{
			return false;
		}
	}
}
