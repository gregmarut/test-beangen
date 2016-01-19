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

public interface Condition
{
	/**
	 * Determines if this condition results to true
	 * 
	 * @param field
	 *            The field of the attribute
	 * @return
	 */
	boolean isTrue(final Field field);
}
