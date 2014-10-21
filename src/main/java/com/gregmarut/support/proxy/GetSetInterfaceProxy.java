/*******************************************************************************
 * Copyright (c) 2013 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.support.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class GetSetInterfaceProxy implements InvocationHandler
{
	// ** Finals **//
	private static final String GET_PREFIX = "get";
	private static final String SET_PREFIX = "set";
	private static final int GET_PREFIX_LENGTH = GET_PREFIX.length();
	private static final int SET_PREFIX_LENGTH = SET_PREFIX.length();
	
	// holds the default key when the key cannot be determined (due to the entire method being the
	// prefix)
	private static final String DEFAULT_KEY = "";
	
	// ** Objects **//
	/**
	 * Contains the map that is responsible for mapping the get/set methods to a value
	 */
	private Map<String, Object> getSetMap;
	
	/**
	 * Constructs a new GetSetInterfaceProxy
	 */
	private GetSetInterfaceProxy()
	{
		getSetMap = new HashMap<String, Object>();
	}
	
	/**
	 * Creates a new proxy class for this handler.
	 * 
	 * @param interfaces
	 * Must be interfaces and not classes
	 * @return New proxy classes
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(final Class<?>... interfaces)
	{
		return (T) Proxy.newProxyInstance(GetSetInterfaceProxy.class.getClassLoader(),
			interfaces, new GetSetInterfaceProxy());
	}
	
	/**
	 * Whenever any method on the interface is called, this method intercepts and handles the
	 * invocation of
	 * the method
	 * 
	 * @param proxy
	 * The instantiated object used for the interface
	 * @param method
	 * The method that was invoked
	 * @param args
	 * The arguments passed into the method
	 * @return Object
	 */
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
	{
		// holds the value to return
		Object value = null;
		
		// holds the method name
		String methodName = method.getName();
		
		// check to see if this method is a getter
		if (methodName.startsWith(GET_PREFIX))
		{
			// get the key
			String key =
				(methodName.length() > GET_PREFIX_LENGTH ? methodName.substring(GET_PREFIX_LENGTH) : DEFAULT_KEY);
			
			// get the value from the map
			value = getSetMap.get(key);
		}
		
		// check to see if this method is a setter and that there is exactly 1 argument for this
		// setter
		else if (methodName.startsWith(SET_PREFIX) && args.length == 1)
		{
			// get the key
			String key =
				(methodName.length() > SET_PREFIX_LENGTH ? methodName.substring(SET_PREFIX_LENGTH) : DEFAULT_KEY);
			
			// set the value in the map
			getSetMap.put(key, args[0]);
		}
		
		return value;
	}
}
