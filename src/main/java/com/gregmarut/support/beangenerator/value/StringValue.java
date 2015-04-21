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
	public String getValue(final Field field)
	{
		return field.getName();
	}
}
