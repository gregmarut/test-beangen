/*******************************************************************************
 * <pre>
 * Copyright (c) 2015 Greg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Greg - initial API and implementation
 * </pre>
 ******************************************************************************/
package com.gregmarut.support.beangenerator.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheManager
{
	private static CacheManager instance;
	
	// ** Objects **//
	// instantiate the logger
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	// holds the map that will store cached objects
	private final Map<Class<?>, Object> beanCache;
	
	private CacheManager()
	{
		beanCache = new ConcurrentHashMap<Class<?>, Object>();
	}
	
	/**
	 * Retrieves an object from the cache
	 * 
	 * @param clazz
	 * @return
	 */
	public Object get(final Class<?> key)
	{
		return beanCache.get(key);
	}
	
	/**
	 * Places an object into the cache
	 * 
	 * @param key
	 * @param value
	 */
	public void put(final Class<?> key, final Object value)
	{
		beanCache.put(key, value);
	}
	
	/**
	 * First, this attempts to pull the object from the cache. If the object does not exist in the cache, it is
	 * attempted to be retrieved and the cache is updated provided that the object was retrieved successfully
	 * 
	 * @param key
	 * @param retrieve
	 * @return
	 */
	public Object getOrRetieve(final Class<?> key, final Retrieve<?> retrieve)
	{
		logger.debug("Attempting to lookup {} from the cache.", key.getName());
		
		// attempt to retrieve the object from the cache
		Object object = beanCache.get(key);
		
		// check to see if the object is null
		if (null == object)
		{
			logger.debug("{} was not found in the cache. Attempting to retrieve...", key.getName());
			
			// attempt to retrieve the object
			object = retrieve.retrieve();
			
			// make sure the object is not null
			if (null != object)
			{
				logger.debug("Adding {} to the cache", key.getName());
				
				// update the cache with this object
				put(key, object);
			}
		}
		else
		{
			logger.debug("Found {} in the cache.", key.getName());
		}
		
		return object;
	}
	
	public static synchronized CacheManager getInstance()
	{
		// check to see if the instance needs to be instantiated
		if (null == instance)
		{
			instance = new CacheManager();
		}
		
		return instance;
	}
}
