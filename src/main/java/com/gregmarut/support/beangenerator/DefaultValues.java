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

import java.util.Date;
import java.util.HashMap;
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
public class DefaultValues
{
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
	
	private final Map<Class<?>, Value<?>> map;
	
	/**
	 * Constructs the default values map
	 */
	public DefaultValues()
	{
		map = new HashMap<Class<?>, Value<?>>();
		
		put(String.class, new StringValue());
		
		put(Integer.class, DEFAULT_INTEGER);
		put(Integer[].class, new Integer[]
		{
			DEFAULT_INTEGER
		});
		put(int.class, DEFAULT_INTEGER);
		put(int[].class, new int[]
		{
			DEFAULT_INTEGER
		});
		
		put(Short.class, DEFAULT_SHORT);
		put(Short[].class, new Short[]
		{
			DEFAULT_SHORT
		});
		put(short.class, DEFAULT_SHORT);
		put(short[].class, new short[]
		{
			DEFAULT_SHORT
		});
		
		put(Float.class, DEFAULT_FLOAT);
		put(Float[].class, new Float[]
		{
			DEFAULT_FLOAT
		});
		put(float.class, DEFAULT_FLOAT);
		put(float[].class, new float[]
		{
			DEFAULT_FLOAT
		});
		
		put(Double.class, DEFAULT_DOUBLE);
		put(Double[].class, new Double[]
		{
			DEFAULT_DOUBLE
		});
		put(double.class, DEFAULT_DOUBLE);
		put(double[].class, new double[]
		{
			DEFAULT_DOUBLE
		});
		
		put(Long.class, DEFAULT_LONG);
		put(Long[].class, new Long[]
		{
			DEFAULT_LONG
		});
		put(long.class, DEFAULT_LONG);
		put(long[].class, new long[]
		{
			DEFAULT_LONG
		});
		
		put(Boolean.class, DEFAULT_BOOLEAN);
		put(Boolean[].class, new Boolean[]
		{
			DEFAULT_BOOLEAN
		});
		put(boolean.class, DEFAULT_BOOLEAN);
		put(boolean[].class, new boolean[]
		{
			DEFAULT_BOOLEAN
		});
		
		put(Byte.class, DEFAULT_BYTE);
		put(Byte[].class, new Byte[]
		{
			DEFAULT_BYTE
		});
		put(byte.class, DEFAULT_BYTE);
		put(byte[].class, new byte[]
		{
			DEFAULT_BYTE
		});
		
		put(Character.class, DEFAULT_CHARACTER);
		put(Character[].class, new Character[]
		{
			DEFAULT_CHARACTER
		});
		put(char.class, DEFAULT_CHARACTER);
		put(char[].class, new char[]
		{
			DEFAULT_CHARACTER
		});
		
		put(Date.class, DEFAULT_DATE);
		
		put(Class.class, DEFAULT_CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public <V> void put(final Class<V> key, final V value)
	{
		// create a new value object
		map.put(key, new StaticValue<V>(value, (Class<V>) value.getClass()));
	}
	
	public <V> void put(final Class<V> key, final Value<V> value)
	{
		// create a new value object
		map.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <V> Value<V> get(final Class<V> key)
	{
		// retrieve the value object from the map
		Value<V> value = (Value<V>) map.get(key);
		return value;
	}
	
	public boolean containsKey(final Class<?> key)
	{
		return map.containsKey(key);
	}
}
