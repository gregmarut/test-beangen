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
package com.gregmarut.support.beangenerator.config;

import java.util.Date;
import java.util.Map;

import com.gregmarut.support.beangenerator.value.StaticValue;
import com.gregmarut.support.beangenerator.value.StringValue;
import com.gregmarut.support.beangenerator.value.Value;

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
public class DefaultValues extends ClassMapContainer<Value<?>>
{
	private static final long serialVersionUID = -5754802711063668704L;
	
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
	 * Sets up the map with all of the default values
	 */
	protected void setupDefaultValues()
	{
		put(String.class, new StringValue());
		
		putStaticValue(Integer.class, DEFAULT_INTEGER);
		putStaticValue(Integer[].class, new Integer[] { DEFAULT_INTEGER });
		putStaticValue(int.class, DEFAULT_INTEGER);
		putStaticValue(int[].class, new int[] { DEFAULT_INTEGER });
		
		putStaticValue(Short.class, DEFAULT_SHORT);
		putStaticValue(Short[].class, new Short[] { DEFAULT_SHORT });
		putStaticValue(short.class, DEFAULT_SHORT);
		putStaticValue(short[].class, new short[] { DEFAULT_SHORT });
		
		putStaticValue(Float.class, DEFAULT_FLOAT);
		putStaticValue(Float[].class, new Float[] { DEFAULT_FLOAT });
		putStaticValue(float.class, DEFAULT_FLOAT);
		putStaticValue(float[].class, new float[] { DEFAULT_FLOAT });
		
		putStaticValue(Double.class, DEFAULT_DOUBLE);
		putStaticValue(Double[].class, new Double[] { DEFAULT_DOUBLE });
		putStaticValue(double.class, DEFAULT_DOUBLE);
		putStaticValue(double[].class, new double[] { DEFAULT_DOUBLE });
		
		putStaticValue(Long.class, DEFAULT_LONG);
		putStaticValue(Long[].class, new Long[] { DEFAULT_LONG });
		putStaticValue(long.class, DEFAULT_LONG);
		putStaticValue(long[].class, new long[] { DEFAULT_LONG });
		
		putStaticValue(Boolean.class, DEFAULT_BOOLEAN);
		putStaticValue(Boolean[].class, new Boolean[] { DEFAULT_BOOLEAN });
		putStaticValue(boolean.class, DEFAULT_BOOLEAN);
		putStaticValue(boolean[].class, new boolean[] { DEFAULT_BOOLEAN });
		
		putStaticValue(Byte.class, DEFAULT_BYTE);
		putStaticValue(Byte[].class, new Byte[] { DEFAULT_BYTE });
		putStaticValue(byte.class, DEFAULT_BYTE);
		putStaticValue(byte[].class, new byte[] { DEFAULT_BYTE });
		
		putStaticValue(Character.class, DEFAULT_CHARACTER);
		putStaticValue(Character[].class, new Character[] { DEFAULT_CHARACTER });
		putStaticValue(char.class, DEFAULT_CHARACTER);
		putStaticValue(char[].class, new char[] { DEFAULT_CHARACTER });
		
		putStaticValue(Date.class, DEFAULT_DATE);
		
		putStaticValue(Class.class, DEFAULT_CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public final <V> void putStaticValue(final Class<V> key, final V value)
	{
		// make sure the value is not null
		if (null == value)
		{
			throw new IllegalArgumentException("value cannot be null.");
		}
		
		// create a new value object
		put(key, new StaticValue<V>(value, (Class<V>) value.getClass()));
	}
}
