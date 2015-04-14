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
package com.gregmarut.support.beangenerator;

public class ClassConversionUtil
{
	private ClassConversionUtil()
	{
		
	}
	
	/**
	 * Converts a primitive data type to its corresponding object class
	 * 
	 * @param clazz
	 * @return Class
	 */
	public static Class<?> convertToNonPrimitive(final Class<?> clazz)
	{
		// make sure the class is not null
		if (null == clazz)
		{
			return null;
		}
		// check all of the primitive types
		else if (int.class.equals(clazz))
		{
			return Integer.class;
		}
		else if (short.class.equals(clazz))
		{
			return Short.class;
		}
		else if (float.class.equals(clazz))
		{
			return Float.class;
		}
		else if (double.class.equals(clazz))
		{
			return Double.class;
		}
		else if (long.class.equals(clazz))
		{
			return Long.class;
		}
		else if (boolean.class.equals(clazz))
		{
			return Boolean.class;
		}
		else if (byte.class.equals(clazz))
		{
			return Byte.class;
		}
		else if (char.class.equals(clazz))
		{
			return Character.class;
		}
		else
		{
			return clazz;
		}
	}
}
