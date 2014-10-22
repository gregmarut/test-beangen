package com.gregmarut.support.beangenerator.rule;

public class FieldNameStartsWithRule<V> extends FieldNameRule<V>
{
	public FieldNameStartsWithRule(final String pattern, final V value)
	{
		super(pattern, value);
	}
	
	@Override
	public boolean isMatch(final Class<?> clazz, final String name)
	{
		// make sure the name is not null
		if (null != name)
		{
			return name.startsWith(pattern);
		}
		else
		{
			return false;
		}
	}
}
