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
package com.gregmarut.support.beangenerator.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class ClassMapContainer<E> implements Serializable
{
	private static final long serialVersionUID = -5674202025647585631L;
	
	// holds the map
	private final Map<Class<?>, E> map;
	
	/**
	 * Constructs the default values map
	 */
	public ClassMapContainer()
	{
		map = new HashMap<Class<?>, E>();
		
		// add the default values to the map
		setupDefaultValues();
	}
	
	public final void put(final Class<?> key, final E value)
	{
		// make sure the value is not null
		if (null == value)
		{
			throw new IllegalArgumentException("value cannot be null.");
		}
		
		// create a new value object
		map.put(key, value);
	}
	
	public final E get(final Class<?> key)
	{
		// retrieve the value object from the map
		return (E) map.get(key);
	}
	
	public final E remove(final Class<?> key)
	{
		// remove the object from the map if it exists
		return (E) map.remove(key);
	}
	
	public final boolean containsKey(final Class<?> key)
	{
		return map.containsKey(key);
	}
	
	public final void clear()
	{
		map.clear();
	}
	
	public final void reset()
	{
		clear();
		setupDefaultValues();
	}
	
	protected abstract void setupDefaultValues();
}
