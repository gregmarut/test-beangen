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
package com.gregmarut.support.beangenerator.value;

import java.util.Deque;

import com.gregmarut.support.beangenerator.model.FieldMember;

/**
 * Adds a layer of abstraction for a value allowing for the customization for how a value is
 * retrieved
 * 
 * @author Greg Marut
 * @param <T>
 */
public interface Value<T>
{
	/**
	 * Returns the class for this value
	 * 
	 * @return
	 */
	Class<T> getType();
	
	/**
	 * Returns the generated value
	 * 
	 * @return
	 */
	T getValue();
	
	/**
	 * Returns the generated value
	 * 
	 * @param fieldMemberStack
	 *        the entire history of field members that have been created up until this point. This
	 *        deque is a shallow copy of the original so deque modifications are allowed
	 * @return
	 */
	T getValue(Deque<FieldMember> fieldMemberStack);
}
