package com.gregmarut.support.beangenerator.rule.condition;

import java.lang.reflect.Field;

public interface Condition
{
	/**
	 * Determines if this condition results to true
	 * 
	 * @param clazz
	 *            The type of value that is being checked
	 * @param field
	 *            The field of the attribute
	 * @return
	 */
	boolean isTrue(final Class<?> clazz, final Field field);
}
