/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg Marut - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.support.beangenerator.rule.condition;

import java.lang.reflect.Field;

/**
 * A condition for checking if a field is a member of a specific class or any parent class in the
 * hierarchy
 * 
 * @author Greg Marut
 */
public class DeclaringAnyClassCondition implements Condition
{
	private final Class<?> declaringClass;
	
	public DeclaringAnyClassCondition(final Class<?> declaringClass)
	{
		this.declaringClass = declaringClass;
	}
	
	@Override
	public boolean isTrue(final Field field, final Object declaringObject)
	{
		return declaringObject.getClass().equals(declaringClass);
	}
}
