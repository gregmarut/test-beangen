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

import java.lang.reflect.Proxy;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gregmarut.support.beangenerator.cache.Cache;
import com.gregmarut.support.beangenerator.cache.Retrieve;
import com.gregmarut.support.beangenerator.config.Configuration;
import com.gregmarut.support.beangenerator.config.InterfaceMapper;
import com.gregmarut.support.beangenerator.proxy.GeneratorInterfaceProxy;
import com.gregmarut.support.beangenerator.rule.Rule;
import com.gregmarut.support.util.ClassConversionUtil;

/**
 * This class is responsible for the actual initialization of a bean object. It uses reflection to cascade an object and
 * populate its fields
 * 
 * @author Greg Marut
 */
public abstract class BeanPropertyInitializer
{
	// ** Objects **//
	// instantiate the logger
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Configuration configuration;
	
	// holds the cache for this BeanPropertyGenerator
	protected final Cache cache;
	
	// holds the stack of instantiated classes to detect and prevent infinite
	// loops
	protected final Deque<Class<?>> instantiationStack;
	
	/**
	 * Constructs a new BeanPropertyInitializer
	 * 
	 * @param configuration
	 */
	BeanPropertyInitializer(final Configuration configuration, final Cache cache)
	{
		// make sure the configuration are not null
		if (null == configuration)
		{
			throw new IllegalArgumentException("configuration cannot be null");
		}
		
		setConfiguration(configuration);
		
		this.instantiationStack = new ArrayDeque<Class<?>>();
		this.cache = cache;
	}
	
	/**
	 * Initializes a class and returns a new instantiated object. All fields in the new object are also instantiated.
	 * 
	 * @param clazz
	 * @return Object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	final <T> T initialize(final Class<T> clazz) throws InstantiationException, IllegalAccessException
	{
		return initialize(clazz, true);
	}
	
	/**
	 * Initializes a class and returns a new instantiated object. All fields in the new object are also instantiated
	 * provided the populate boolean is set to true.
	 * 
	 * @param clazz
	 * @param populate
	 * @return Object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	final <T> T initialize(final Class<T> clazz, final boolean populate) throws InstantiationException,
			IllegalAccessException
	{
		logger.debug("Initializing {}", clazz.getName());
		
		// holds the object to return
		final T object;
		
		// make sure this class does not already exist in the instantiation
		// stack
		if (!instantiationStack.contains(clazz))
		{
			// instantiate a new version of this method
			object = instantiate(clazz);
			
			// check to see if caching is enabled
			if (configuration.isCache())
			{
				logger.debug("Adding {} to the cache", clazz.getName());
				
				// add this object to the model map
				cache.put(clazz, object);
			}
			
			// make sure the new object is not null
			// a new object can only be null if it was specifically defined as
			// null in the
			// configuration.getDefaultValues()
			if (null != object && !Proxy.isProxyClass(object.getClass()))
			{
				// push this class onto the stack
				instantiationStack.push(clazz);
				
				if (populate)
				{
					// populate the object via methods
					populate(object);
				}
				
				// remove this class from the stack
				instantiationStack.pop();
			}
		}
		else
		{
			// an infinite loop was detected
			logger.info("Cyclical dependency detected while attempting to initialize {}. Skipping object population.",
					clazz.getName());
			object = null;
		}
		
		// return the object
		return object;
	}
	
	/**
	 * Initializes an object. All fields in the new object are also instantiated.
	 * 
	 * @param object
	 * @return Object
	 */
	final Object initialize(final Object object)
	{
		// make sure the new object is not null
		// a new object can only be null if it was specifically defined as null
		// in the
		// configuration.getDefaultValues()
		if (null != object)
		{
			logger.debug("Initializing {}", object.getClass().getName());
			
			// push this class onto the stack
			instantiationStack.push(object.getClass());
			
			// populate the object via methods
			populate(object);
			
			// remove this class from the stack
			instantiationStack.pop();
		}
		
		// return the new object
		return object;
	}
	
	/**
	 * Fully populates an object
	 * 
	 * @param object
	 */
	protected abstract void populate(final Object object);
	
	/**
	 * Instantiates a new instance of the class. If the class is an interface, this method will attempt to lookup the
	 * corresponding concrete class in the {@link InterfaceMapper}.
	 * 
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T instantiate(final Class<T> clazz) throws InstantiationException, IllegalAccessException
	{
		// holds the object to return
		T newObject;
		
		// check to see if this class is an interface
		if (clazz.isInterface())
		{
			// attempt to map the interface to a concrete class to instantiate
			// instead
			Class<T> concreteClass = (Class<T>) configuration.getInterfaceMapper().get(clazz);
			
			if (null != concreteClass)
			{
				logger.debug("{} found to replace interface {}", concreteClass.getName(), clazz.getName());
				
				// instantiate a new instance of the concrete class
				newObject = initialize(concreteClass);
			}
			else
			{
				// check to see if proxies should be generator for unmapped
				// interfaces
				if (configuration.getProxyUnmappedInterfaces())
				{
					// create a new proxy for this interface
					newObject = GeneratorInterfaceProxy.createProxy(configuration, clazz);
				}
				else
				{
					throw new InstantiationException("Interface " + clazz.getName()
							+ " does not have mapped concrete class in " + InterfaceMapper.class.getName());
				}
			}
		}
		// check to see if the class is an enum
		else if (clazz.isEnum())
		{
			// get the array of enum constants
			Enum<?>[] enumValues = ((Class<Enum<?>>) clazz).getEnumConstants();
			
			// make sure there are values
			if (enumValues.length > 0)
			{
				// create a new enum value from the first value
				newObject = (T) enumValues[0];
			}
			else
			{
				newObject = null;
			}
		}
		else
		{
			// check to see if this value exists in the default values map
			if (configuration.getDefaultValues().containsKey(clazz))
			{
				logger.debug("Found default value for {}", clazz.getName());
				
				// retrieve the default value
				newObject = (T) configuration.getDefaultValues().get(clazz).getValue();
			}
			else
			{
				// make sure this class is not literally an instance of a class
				if (!clazz.equals(Class.class))
				{
					// instantiate the object
					newObject = clazz.newInstance();
				}
				else
				{
					logger.debug("Cannot instantiate a Class - assigning null.");
					newObject = null;
				}
			}
		}
		
		return newObject;
	}
	
	/**
	 * Checks the {@link configuration.getRuleMapping()} to determine if there are any {@link Rule} that match this
	 * specific setter method. If a match is found, the {@link Rule} is returned.
	 * 
	 * @param name
	 * @param clazz
	 * @return Rule
	 */
	protected Rule<?> checkForMatchingRule(final String name, final Class<?> clazz)
	{
		// holds the value to return
		Rule<?> rule = null;
		
		// make sure the rule mapping object is not null
		if (null != configuration.getRuleMapping())
		{
			// convert the class from its primitive value if applicable,
			// otherwise use the original
			// value
			// The reason that primitives have to be cast up is because Generics
			// does not support
			// primitives.
			Class<?> nonPrimitiveClass = ClassConversionUtil.convertToNonPrimitive(clazz);
			
			// check to see if the rule mapping contains rules for this
			// parameter type
			if (configuration.getRuleMapping().contains(nonPrimitiveClass))
			{
				// get the list of rules from the rule mapping based on this
				// parameter type
				List<Rule<?>> rules = configuration.getRuleMapping().get(nonPrimitiveClass);
				
				// for every rule in the list or until a rule is found
				for (int i = 0; i < rules.size() && null == rule; i++)
				{
					// get the current field matching rule
					Rule<?> currentRule = rules.get(i);
					
					// check to see if this rule is a match
					if (currentRule.isMatch(nonPrimitiveClass, name))
					{
						rule = currentRule;
					}
				}
			}
		}
		
		return rule;
	}
	
	public final void setConfiguration(final Configuration configuration)
	{
		if (null == configuration)
		{
			throw new IllegalArgumentException("configuration cannot be null.");
		}
		
		this.configuration = configuration;
	}
	
	public final Configuration getConfiguration()
	{
		return configuration;
	}
	
	/**
	 * Defines a blueprint for how to retrieve an object by calling the initialize method
	 * 
	 * @author Greg Marut
	 */
	protected class RetrieveByInitialize implements Retrieve<Object>
	{
		private Class<?> clazz;
		
		public RetrieveByInitialize(final Class<?> clazz)
		{
			this.clazz = clazz;
		}
		
		@Override
		public Object retrieve()
		{
			try
			{
				return initialize(clazz);
			}
			catch (InstantiationException e)
			{
				final String message = "Failed to instantiate: " + clazz.getName();
				logger.warn(message, e);
				return null;
			}
			catch (IllegalAccessException e)
			{
				logger.warn(e.getMessage(), e);
				return null;
			}
		}
	}
}
