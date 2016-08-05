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
 * Represents a null value
 * 
 * @author Greg Marut
 * @param <V>
 */
public class NullValue<V> implements Value<V>
{
	private final Class<V> type;
	
	public NullValue(final Class<V> type)
	{
		this.type = type;
	}
	
	@Override
	public Class<V> getType()
	{
		return type;
	}
	
	@Override
	public V getValue()
	{
		return null;
	}
	
	@Override
	public V getValue(Deque<FieldMember> fieldMemberStack)
	{
		return null;
	}
}
