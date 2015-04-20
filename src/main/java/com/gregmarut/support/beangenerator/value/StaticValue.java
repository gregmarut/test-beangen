package com.gregmarut.support.beangenerator.value;

/**
 * Represents a value that never changes
 * 
 * @author Greg Marut
 * @param <V>
 */
public class StaticValue<V> implements Value<V>
{
	public final V value;
	
	public StaticValue(final V value)
	{
		this.value = value;
	}
	
	@Override
	public V getValue()
	{
		return value;
	}
}
