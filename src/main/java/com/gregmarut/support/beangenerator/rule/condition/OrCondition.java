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
 * Considered true if any of the conditions are true, false otherwise
 * 
 * @author Greg Marut
 */
public class OrCondition extends MultipleConditions
{
	public OrCondition(final Condition... conditions)
	{
		super(conditions);
	}
	
	@Override
	public boolean isTrue(final Field field)
	{
		// for each of the conditions
		for (Condition rule : conditions)
		{
			// if any of the conditions are true
			if (rule.isTrue(field))
			{
				return true;
			}
		}
		
		// none of these conditions were true
		return false;
	}
}
