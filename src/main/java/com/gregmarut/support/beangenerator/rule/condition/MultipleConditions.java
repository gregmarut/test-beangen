package com.gregmarut.support.beangenerator.rule.condition;

public abstract class MultipleConditions implements Condition
{
	// holds the array of conditions that must all be met to be considered valid
	protected final Condition[] conditions;
	
	public MultipleConditions(final Condition... conditions)
	{
		// make sure the array is not null
		if (null == conditions)
		{
			throw new IllegalArgumentException("conditions cannot be null");
		}
		
		// for each of the conditions
		for (Condition rule : conditions)
		{
			// make sure the rule is not null
			if (null == rule)
			{
				throw new IllegalArgumentException("conditions cannot contain a null rule");
			}
		}
		
		this.conditions = conditions;
	}
}
