package com.gregmarut.support.beangenerator.rule;

public class FieldNameEndsWithRule<V> extends FieldNameRule<V>
{
	public FieldNameEndsWithRule(final String pattern, final V value)
	{
		super(pattern, value);
	}
	
	@Override
	public boolean isMatch(final Class<?> clazz, final String name)
	{
		//make sure the name is not null
		if(null != name)
		{
			return name.endsWith(pattern);
		}
		else
		{
			return false;
		}
	}
}
