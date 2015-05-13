package com.gregmarut.support.beangenerator.rule.condition;

import java.lang.reflect.Field;

public class UnionCondition extends MultipleConditions
{
	@Override
	public boolean isTrue(final Class<?> clazz, final Field field)
	{
		// for each of the conditions
		for (Condition rule : conditions)
		{
			// if any of the conditions are true
			if (rule.isTrue(clazz, field))
			{
				return true;
			}
		}
		
		// none of these conditions were true
		return false;
	}
}
