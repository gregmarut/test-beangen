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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayDeque;
import java.util.Collection;
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
import com.gregmarut.support.util.ReflectionUtil;

/**
 * This class is responsible for the actual initialization of a bean object. It uses reflection to cascade an object
 * looking for all declared fields and creates a new instance of that class.
 * 
 * @author Greg Marut
 */
public class BeanPropertyInitializer
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
	 * Fully populates an object and traverses the all of the fields recursively
	 * 
	 * @param object
	 */
	protected final void populate(final Object object)
	{
		// retrieve a list of all of the methods defined in this class
		Field[] fields = ReflectionUtil.getAllFields(object);
		
		// set the data on the object
		setData(object, fields);
	}
	
	/**
	 * Sets the data on the object
	 * 
	 * @param obj
	 * @param setterMethods
	 */
	protected final void setData(final Object obj, final Field[] fields)
	{
		// for each of the fields in the list
		for (Field field : fields)
		{
			// make sure this field is not transient and its not final
			if (!Modifier.isTransient(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
			{
				// get the type of this field
				Class<?> clazz = field.getType();
				
				try
				{
					// holds the value to set for this field
					Object value;
					
					// attempt to see if a rule generated value exists for this parameter type and
					// setter method
					Rule<?> rule = checkForMatchingRule(field, clazz);
					
					// check to see if a value was found based on the rules
					if (null == rule)
					{
						// holds the value to set in the setter method
						value = getValue(clazz, field);
					}
					else
					{
						logger.debug("Rule found for \"{}\":{}", field.getName(), clazz.getName());
						
						// set the value to the value defined in the rule
						value = rule.getValue().getValue(field);
					}
					
					// set the value on the object
					field.setAccessible(true);
					field.set(obj, value);
				}
				catch (InstantiationException e)
				{
					// This condition typically occurs with data types that aren't currently
					// supported.
					// Info log level is used here rather than Error or Warn because there are cases
					// where we don't mind if some fields are not initialized.
					// One type that we've seen is JAXBElement, which can't
					// be set to generated values without more info (such as namespace).
					// However, some other fields in the object that are not of type JAXBElement
					// will get set, and depending on the test case, this may be fine.
					logger.info("Could not intialize property named: {} of type: {} in object of type: {}",
							field.getName(), clazz.getName(), obj.getClass().getCanonicalName());
				}
				catch (IllegalArgumentException e)
				{
					logger.error(e.getMessage(), e);
				}
				catch (IllegalAccessException e)
				{
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * Converts a parameter type to a value to be set onto the method.
	 * 
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected final Object getValue(final Class<?> clazz) throws InstantiationException, IllegalAccessException
	{
		return getValue(clazz, null);
	}
	
	/**
	 * Converts a parameter type to a value to be set onto the method.
	 * 
	 * @param clazz
	 * @param field
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected final Object getValue(final Class<?> clazz, final Field field) throws InstantiationException,
			IllegalAccessException
	{
		// holds the value of the object to return
		final Object obj;
		
		// make sure the parameter is not null
		if (null != clazz)
		{
			// check to see if this parameter is a type of collection
			if (Collection.class.isAssignableFrom(clazz))
			{
				// instantiate a new collection object
				@SuppressWarnings("unchecked")
				Collection<Object> collection = (Collection<Object>) instantiate(clazz);
				
				// populate the collection
				populateCollection(collection, field);
				
				// assign the collection as the object to set into the method
				obj = collection;
			}
			else
			{
				// check to see if this value exists in the default values map
				if (configuration.getDefaultValues().containsKey(clazz))
				{
					// make sure the field is not null
					if (null != field)
					{
						logger.debug("Found default value for \"{}\":{}", field.getName(), clazz.getName());
						
						// retrieve the default value
						obj = configuration.getDefaultValues().get(clazz).getValue(field);
					}
					else
					{
						// retrieve the default value
						obj = configuration.getDefaultValues().get(clazz).getValue();
					}
				}
				else
				{
					// create the object that instructs how to retrieve the object
					Retrieve<Object> retrieve = new RetrieveByInitialize(clazz);
					
					// check to see if caching is enabled
					if (configuration.useCache(clazz))
					{
						obj = cache.getOrRetieve(clazz, retrieve);
					}
					else
					{
						obj = retrieve.retrieve();
					}
				}
			}
		}
		else
		{
			obj = null;
		}
		
		return obj;
	}
	
	/**
	 * Searches all of the fields of a class and attempts to pick out the fields that return a collection. For each
	 * collection found, this method populates it with test data
	 * 
	 * @param fields
	 */
	protected final void populateCollection(final Collection<Object> collection, final Field field)
	{
		try
		{
			// make sure the collection is not null and that it is not a proxy
			if (null != collection && !Proxy.isProxyClass(collection.getClass()))
			{
				// extract the generic classes for this field
				List<Class<?>> genericClasses = ReflectionUtil.extractGenericClasses(field);
				
				// make sure the generic class was found
				if (!genericClasses.isEmpty())
				{
					// populate this collection
					populateCollection(collection, genericClasses.get(0));
				}
				else
				{
					logger.debug(
							"Could not populate the collection of {} because the generic class type could not be determined.",
							field.getName());
				}
			}
		}
		catch (InstantiationException e)
		{
			logger.error(e.getMessage(), e);
		}
		catch (IllegalAccessException e)
		{
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Populates a collection of objects with the given class type
	 * 
	 * @param collection
	 * @param clazz
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected final void populateCollection(final Collection<Object> collection, Class<?> clazz)
			throws InstantiationException, IllegalAccessException
	{
		logger.debug("Populating {} with objects of type {}", collection.getClass().getName(), clazz.getName());
		
		// for the specific number of times to auto fill lists
		for (int i = 0; i < configuration.getCollectionAutoFillCount(); i++)
		{
			// get the value for this class type
			Object object = getValue(clazz);
			
			// make sure the object is not null
			if (null != object)
			{
				// add the object to the collection
				collection.add(object);
			}
		}
	}
	
	/**
	 * Checks the {@link configuration.getRuleMapping()} to determine if there are any {@link Rule} that match this
	 * specific field name. If a match is found, the {@link Rule} is returned.
	 * 
	 * @param field
	 * @param clazz
	 * @return Rule
	 */
	protected final Rule<?> checkForMatchingRule(final Field field, final Class<?> clazz)
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
					if (currentRule.isTrue(nonPrimitiveClass, field))
					{
						rule = currentRule;
					}
				}
			}
		}
		
		return rule;
	}
	
	/**
	 * Sets the configuration
	 * 
	 * @param configuration
	 */
	public final void setConfiguration(final Configuration configuration)
	{
		if (null == configuration)
		{
			throw new IllegalArgumentException("configuration cannot be null.");
		}
		
		this.configuration = configuration;
	}
	
	/**
	 * Retrieves the configuration
	 * 
	 * @return
	 */
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
