package com.gregmarut.support.beangenerator.value;

import java.lang.reflect.Field;

/**
 * Represents a value that never changes
 * 
 * @author Greg Marut
 * @param <V>
 */
public class StaticValue<V> implements Value<V>
{
	private final Class<V> type;
	private final V value;
	
	public StaticValue(final V value, final Class<V> type)
	{
		this.value = value;
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
		return value;
	}
	
	@Override
	public V getValue(Field field)
	{
		return value;
	}
}
