package com.gregmarut.support.beangenerator.value;

import java.lang.reflect.Field;

/**
 * Represents a null value
 * 
 * @author Greg Marut
 * @param <V>
 */
public class NullValue<V> implements Value<V>
{
	private final Class<V> type;
	
	public NullValue(final Class<V> type)
	{
		this.type = type;
	}
	
	@Override
	public Class<V> getType()
	{
		return type;
	}
	
	@Override
	public V getValue()
	{
		return null;
	}
	
	@Override
	public V getValue(final Field field)
	{
		return null;
	}
}