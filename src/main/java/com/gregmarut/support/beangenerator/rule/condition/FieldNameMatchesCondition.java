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

public class FieldNameMatchesCondition extends FieldNameCondition
{
	public FieldNameMatchesCondition(String pattern)
	{
		super(pattern);
	}
	
	@Override
	public boolean isTrue(final Field field)
	{
		// check to see if the field names match
		return pattern.equals(field.getName());
	}
}
