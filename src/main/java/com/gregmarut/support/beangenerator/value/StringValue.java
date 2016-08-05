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
package com.gregmarut.support.beangenerator.value;

import java.lang.reflect.Field;

/**
 * The default implementation for how strings are generated
 * 
 * @author Greg Marut
 */
public class StringValue implements Value<String>
{
	@Override
	public Class<String> getType()
	{
		return String.class;
	}
	
	@Override
	public String getValue()
	{
		return new String();
	}
	
	@Override
	public String getValue(final Field field, final Object declaringObject)
	{
		return field.getName();
	}
}
