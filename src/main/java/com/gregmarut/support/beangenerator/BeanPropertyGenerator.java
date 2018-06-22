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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.gregmarut.support.beangenerator.cache.Cache;
import com.gregmarut.support.beangenerator.cache.Retrieve;
import com.gregmarut.support.beangenerator.config.CacheOverride;
import com.gregmarut.support.beangenerator.config.Configuration;
import com.gregmarut.support.beangenerator.config.DefaultValues;
import com.gregmarut.support.beangenerator.config.InterfaceMapper;
import com.gregmarut.support.beangenerator.model.FieldMember;
import com.gregmarut.support.beangenerator.rule.RuleMapping;

/**
 * <pre>
 * This class generates beans and cascades all of its fields to fully populate a bean with test data. 
 * 
 * Whenever a new field is initialized, the {@link DefaultValues}, is first checked to see if a matching type is found. 
 * If no type has been set as the default, a new instance is created and set. However, unless otherwise overridden in 
 * the {@link DefaultValues}, String fields are handled differently. By default, A String field's default value is set 
 * to the name of the field. 
 * 
 * Collections are automatically populated with X number of objects of the specified generic type where X is 
 * <i>collectionAutoFillCount</i> in the configuration. If no generic is specified for the collection, it is not 
 * automatically populated with objects.
 * </pre>
 * 
 * @author Greg Marut
 */
public final class BeanPropertyGenerator
{
	// ** Finals **//
	// holds the number of objects to auto populate into a collection whenever
	// one is created
	public static final int DEFAULT_COLLECTION_AUTO_FILL_COUNT = 3;
	
	// ** Objects **//
	// holds the cache for this BeanPropertyGenerator
	private final Cache cache;
	
	// holds the configuration for this generator
	private final Configuration configuration;
	
	/**
	 * Constructs a new BeanPropertyGenerator object Bean caching is enabled by default
	 */
	public BeanPropertyGenerator()
	{
		this(true, false);
	}
	
	/**
	 * Constructs a new BeanPropertyGenerator object
	 * 
	 * @param useCache
	 *        Determines whether or not to cache beans once they are created for faster performance.
	 *        Be advised that
	 *        once caching is enabled, any objects that are created again within the same scope
	 *        reference the
	 *        originally created object. If the referenced object was previously modified, all
	 *        consequent calls
	 *        return the same modified object.
	 */
	public BeanPropertyGenerator(final boolean useCache)
	{
		this(useCache, false);
	}
	
	/**
	 * Constructs a new BeanPropertyGenerator object
	 * 
	 * @param useCache
	 *        Determines whether or not to cache beans once they are created for faster performance.
	 *        Be advised that
	 *        once caching is enabled, any objects that are created again within the same scope
	 *        reference the
	 *        originally created object. If the referenced object was previously modified, all
	 *        consequent calls
	 *        return the same modified object.
	 * @param proxyUnmappedInterfaces
	 *        Determines if interfaces that have not been mapped should be instantiated using
	 *        dynamic proxies. These
	 *        proxies will then generate any objects that are returned from the interfaces
	 */
	public BeanPropertyGenerator(final boolean useCache, final boolean proxyUnmappedInterfaces)
	{
		// assign the configuration
		this.configuration = new Configuration();
		
		// assign the default values in the configuration
		configuration.setDefaultValues(new DefaultValues());
		configuration.setInterfaceMapper(new InterfaceMapper());
		configuration.setRuleMapping(new RuleMapping());
		configuration.setCacheOverride(new CacheOverride());
		configuration.setProxyUnmappedInterfaces(proxyUnmappedInterfaces);
		configuration.setCollectionAutoFillCount(DEFAULT_COLLECTION_AUTO_FILL_COUNT);
		configuration.setCache(useCache);
		
		// instantiate the new cache for this instance
		this.cache = new Cache();
	}
	
	/**
	 * Constructs a new BeanPropertyGenerator object
	 * 
	 * @param configuration
	 *        Holds the configuration that will determine how this generator will function
	 */
	public BeanPropertyGenerator(final Configuration configuration)
	{
		// assign the configuration
		this.configuration = configuration;
		
		// instantiate the new cache for this instance
		this.cache = new Cache();
	}
	
	/**
	 * Retrieves an object from the map of bean objects. If the object does not yet exist in the
	 * map, it is initialized
	 * and returned
	 * 
	 * @param clazz
	 * @return Object
	 */
	public <T> T get(final Class<T> clazz)
	{
		return get(clazz, true);
	}
	
	/**
	 * Retrieves an object from the map of bean objects. If the object does not yet exist in the
	 * map, it is initialized
	 * and returned
	 * 
	 * @param clazz
	 * @param populate
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> clazz, final boolean populate)
	{
		// create the object that instructs how to retrieve the object
		Retrieve<T> retrieve = new Retrieve<T>()
		{
			@Override
			public T retrieve()
			{
				try
				{
					// attempt to initialize the new model object
					return getBeanPropertyInitializer().initialize(clazz, populate, new ArrayDeque<FieldMember>());
				}
				catch (InstantiationException e)
				{
					throw new BeanInitializationException(e);
				}
				catch (IllegalAccessException e)
				{
					throw new BeanInitializationException(e);
				}
			}
		};
		
		// check to see if caching is enabled
		if (configuration.useCache(clazz))
		{
			return (T) cache.getOrRetieve(clazz, retrieve);
		}
		else
		{
			return retrieve.retrieve();
		}
	}
	
	/**
	 * Creates a list and populates it with objects of the specified class type
	 * 
	 * @param clazz
	 *        The class to instantiate
	 * @return
	 */
	public <T> List<T> getList(final Class<T> clazz)
	{
		return getList(clazz, configuration.getCollectionAutoFillCount());
	}
	
	/**
	 * Creates a list and populates it with objects of the specified class type
	 * 
	 * @param clazz
	 *        The class to instantiate
	 * @param count
	 *        How many objects to put into the list
	 * @return
	 */
	public <T> List<T> getList(final Class<T> clazz, final int count)
	{
		List<T> list = new ArrayList<T>(count);
		
		for (int i = 0; i < count; i++)
		{
			// get a new populated instance of the object
			T obj = get(clazz);
			
			// add the object to the list
			list.add(obj);
		}
		
		return list;
	}
	
	/**
	 * Populates an object
	 * 
	 * @param object
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T> T populate(final T object)
	{
		// attempt to initialize the new model object
		return (T) getBeanPropertyInitializer().initialize(object, new ArrayDeque<FieldMember>());
	}
	
	/**
	 * Returns the configuration that are associated with this bean property generator
	 * 
	 * @return
	 */
	public Configuration getConfiguration()
	{
		return configuration;
	}
	
	/**
	 * Return the instance of the cache object
	 * 
	 * @return
	 */
	public Cache getCache()
	{
		return cache;
	}
	
	/**
	 * Checks to see if a bean property initializer was set
	 * 
	 * @return
	 */
	protected final BeanPropertyInitializer getBeanPropertyInitializer()
	{
		// create a new instance of the initializer so that it is thread safe.
		return new BeanPropertyInitializer(configuration, cache);
	}
}
