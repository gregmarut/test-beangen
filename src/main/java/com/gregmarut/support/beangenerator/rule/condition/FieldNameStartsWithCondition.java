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

import java.lang.reflect.Field;

public class FieldNameStartsWithCondition extends FieldNameCondition
{
	public FieldNameStartsWithCondition(final String pattern)
	{
		super(pattern);
	}
	
	@Override
	public boolean isTrue(final Field field, final Object declaringObject)
	{
		// make sure the name is not null
		if (null != field.getName())
		{
			return field.getName().startsWith(pattern);
		}
		else
		{
			return false;
		}
	}
}
