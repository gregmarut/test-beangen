package com.gregmarut.support.beangenerator.rule.condition;

import java.lang.reflect.Field;

public class IntersectionCondition extends MultipleConditions
{
	@Override
	public boolean isTrue(final Class<?> clazz, final Field field)
	{
		// for each of the conditions
		for (Condition rule : conditions)
		{
			// check if any condition is false
			if (!rule.isTrue(clazz, field))
			{
				return false;
			}
		}
		
		// all of the conditions returned true
		return true;
	}
}
