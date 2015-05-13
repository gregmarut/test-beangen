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
import java.util.Collection;
import java.util.List;

import com.gregmarut.support.beangenerator.cache.Cache;
import com.gregmarut.support.beangenerator.cache.Retrieve;
import com.gregmarut.support.beangenerator.config.Configuration;
import com.gregmarut.support.beangenerator.rule.Rule;
import com.gregmarut.support.util.ReflectionUtil;

/**
 * This class is responsible for the actual initialization of a bean object. It uses reflection to cascade an object
 * looking for all declared fields and create a new instance of that class.
 * 
 * @author Greg Marut
 */
public final class BeanPropertyFieldInitializer extends BeanPropertyInitializer
{
	/**
	 * Constructs a new BeanPropertyInitializer
	 * 
	 * @param configuration
	 */
	BeanPropertyFieldInitializer(final Configuration configuration, final Cache cache)
	{
		super(configuration, cache);
	}
	
	@Override
	protected void populate(final Object object)
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
	private void setData(final Object obj, final Field[] fields)
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
	private Object getValue(final Class<?> clazz) throws InstantiationException, IllegalAccessException
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
	private Object getValue(final Class<?> clazz, final Field field) throws InstantiationException,
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
	private void populateCollection(final Collection<Object> collection, final Field field)
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
	private void populateCollection(final Collection<Object> collection, Class<?> clazz) throws InstantiationException,
			IllegalAccessException
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
	protected Rule<?> checkForMatchingRule(final Field field, final Class<?> clazz)
	{
		return super.checkForMatchingRule(field.getName(), clazz);
	}
}
