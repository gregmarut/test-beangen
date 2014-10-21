/*******************************************************************************
 * Copyright (c) 2013 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.support.beangenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * A map of classes that hold the default objects for setting on fields in 
 * a model object. Custom values can be added to this class by calling the
 * {@link Map}'s "put" method.
 * 
 * By default, Strings have been purposely omitted from the default values.
 * A String field's default value is the name of the field that it is assigning/
 * </pre>
 * 
 * @author Greg Marut
 */
public class DefaultValues extends HashMap<Class<?>, Object>
{
	// ** Finals **//
	private static final long serialVersionUID = 5182886309843090046L;
	
	// holds the initial default values
	public static final Integer DEFAULT_INTEGER = 1;
	public static final Short DEFAULT_SHORT = 1;
	public static final Float DEFAULT_FLOAT = 1.0f;
	public static final Double DEFAULT_DOUBLE = 1.0;
	public static final Long DEFAULT_LONG = 1L;
	public static final Boolean DEFAULT_BOOLEAN = true;
	public static final Byte DEFAULT_BYTE = (byte) 0x01;
	public static final Character DEFAULT_CHARACTER = 'A';
	public static final Date DEFAULT_DATE = new Date();
	public static final Class<?> DEFAULT_CLASS = Class.class;
	
	/**
	 * Constructs the default values map
	 */
	public DefaultValues()
	{
		super.put(Integer.class, DEFAULT_INTEGER);
		super.put(int.class, DEFAULT_INTEGER);
		
		super.put(Short.class, DEFAULT_SHORT);
		super.put(short.class, DEFAULT_SHORT);
		
		super.put(Float.class, DEFAULT_FLOAT);
		super.put(float.class, DEFAULT_FLOAT);
		
		super.put(Double.class, DEFAULT_DOUBLE);
		super.put(double.class, DEFAULT_DOUBLE);
		
		super.put(Long.class, DEFAULT_LONG);
		super.put(long.class, DEFAULT_LONG);
		
		super.put(Boolean.class, DEFAULT_BOOLEAN);
		super.put(boolean.class, DEFAULT_BOOLEAN);
		
		super.put(Byte.class, DEFAULT_BYTE);
		super.put(byte.class, DEFAULT_BYTE);
		
		super.put(Character.class, DEFAULT_CHARACTER);
		super.put(char.class, DEFAULT_CHARACTER);
		
		super.put(Date.class, DEFAULT_DATE);
		
		super.put(Class.class, DEFAULT_CLASS);
	}
}
