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
package com.gregmarut.support.beangenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds a map of interface objects as well as their corresponding concrete object to
 * instantiate. Since interfaces cannot be directly instantiated, this class will substitute
 * a concrete object whenever an interface is found.
 * 
 * @author Greg Marut
 */
public class InterfaceMapper extends HashMap<Class<?>, Class<?>>
{
	// ** Finals **//
	private static final long serialVersionUID = -7430365572672661258L;
	
	public static final Class<?> DEFAULT_COLLECTION = LinkedList.class;
	public static final Class<?> DEFAULT_LIST = LinkedList.class;
	public static final Class<?> DEFAULT_MAP = HashMap.class;
	public static final Class<?> DEFAULT_SET = HashSet.class;
	
	/**
	 * Constructs the InterfaceMapper
	 */
	public InterfaceMapper()
	{
		super.put(Collection.class, DEFAULT_COLLECTION);
		super.put(List.class, DEFAULT_LIST);
		super.put(Map.class, DEFAULT_MAP);
		super.put(Set.class, DEFAULT_SET);
	}
}
