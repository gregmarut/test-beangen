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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds a map of interface objects as well as their corresponding concrete object to instantiate. Since interfaces
 * cannot be directly instantiated, this class will substitute a concrete object whenever an interface is found.
 * 
 * @author Greg Marut
 */
public class InterfaceMapper
{
	@SuppressWarnings("rawtypes")
	public static final Class<? extends Collection> DEFAULT_COLLECTION = LinkedList.class;
	@SuppressWarnings("rawtypes")
	public static final Class<? extends List> DEFAULT_LIST = LinkedList.class;
	@SuppressWarnings("rawtypes")
	public static final Class<? extends Map> DEFAULT_MAP = HashMap.class;
	@SuppressWarnings("rawtypes")
	public static final Class<? extends Set> DEFAULT_SET = HashSet.class;
	
	// holds the map that stores the class to value mapping
	private final Map<Class<?>, Class<?>> map;
	
	/**
	 * Constructs the InterfaceMapper
	 */
	public InterfaceMapper()
	{
		map = new HashMap<Class<?>, Class<?>>();
		
		// add the default values to the map
		setupDefaultValues();
	}
	
	/**
	 * Sets up the map with all of the default values
	 */
	protected void setupDefaultValues()
	{
		put(Collection.class, DEFAULT_COLLECTION);
		put(List.class, DEFAULT_LIST);
		put(Map.class, DEFAULT_MAP);
		put(Set.class, DEFAULT_SET);
	}
	
	public final <V> void put(final Class<V> key, final Class<? extends V> value)
	{
		// make sure the value is not null
		if (null == value)
		{
			throw new IllegalArgumentException("value cannot be null.");
		}
		
		// create a new value object
		map.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public final <V> Class<V> get(final Class<V> key)
	{
		// retrieve the value object from the map
		return (Class<V>) map.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public final <V> Class<V> remove(final Class<V> key)
	{
		// remove the object from the map if it exists
		return (Class<V>) map.remove(key);
	}
	
	public final boolean containsKey(final Class<?> key)
	{
		return map.containsKey(key);
	}
	
	public final void clear()
	{
		map.clear();
	}
}
