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
package com.gregmarut.support.beangenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.gregmarut.support.beangenerator.cache.CacheManager;
import com.gregmarut.support.beangenerator.cache.Retrieve;
import com.gregmarut.support.beangenerator.rule.Rule;

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
	 * @param properties
	 */
	BeanPropertyFieldInitializer(final Properties properties)
	{
		super(properties);
	}
	
	protected void populate(final Object object)
	{
		// retrieve a list of all of the methods defined in this class
		Field[] fields = getAllFields(object);
		
		// set the data on the object
		setData(object, fields);
		
		// populate the collections in this object with test data
		populateCollections(object, fields);
	}
	
	/**
	 * Retrieve all of the fields for a given object including any parent classes
	 * 
	 * @param object
	 * @return
	 */
	private Field[] getAllFields(final Object object)
	{
		// holds the list of fields
		List<Field> allFields = new ArrayList<Field>();
		
		// holds the class to inspect
		Class<?> clazz = object.getClass();
		
		// while the class is not null
		while (null != clazz)
		{
			// retrieve all of the declared field for this class and add them to the list
			Field[] fields = clazz.getDeclaredFields();
			allFields.addAll(Arrays.asList(fields));
			
			// read this class' superclass next if one exists
			clazz = clazz.getSuperclass();
		}
		
		return allFields.toArray(new Field[allFields.size()]);
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
						value = getValue(field, clazz);
					}
					else
					{
						logger.debug("Rule found for \"{}\":{}", field.getName(), clazz.getName());
						
						// set the value to the value defined in the rule
						value = rule.getValue();
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
	 * @param setterMethod
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getValue(final Field field, final Class<?> clazz) throws InstantiationException,
			IllegalAccessException
	{
		// holds the value of the object to return
		final Object value;
		
		// make sure the parameter is not null
		if (null != clazz)
		{
			// check to see if this parameter is a string
			if (clazz.equals(String.class))
			{
				// create the value to assign to this string field
				String stringValue;
				
				// check to see if the default string should be used
				if (null == properties.getDefaultValues().get(String.class))
				{
					// assign the value to the field name
					stringValue = field.getName();
				}
				else
				{
					// assign the value to the user specified default string
					stringValue = (String) properties.getDefaultValues().get(String.class);
				}
				
				// set the value to this string
				value = stringValue;
			}
			// check to see if this parameter is a type of collection
			else if (Collection.class.isAssignableFrom(clazz))
			{
				// instantiate a new collection object
				Collection<?> collection = (Collection<?>) instantiate(clazz);
				
				// assign the collection as the object to set into the method
				value = collection;
			}
			else
			{
				// check to see if this value exists in the default values map
				if (properties.getDefaultValues().containsKey(clazz))
				{
					logger.debug("Found default value for \"{}\":{}", field.getName(), clazz.getName());
					
					value = properties.getDefaultValues().get(clazz);
				}
				else
				{
					// create the object that instructs how to retrieve the object
					Retrieve<Object> retrieve = new RetrieveByInitialize(clazz);
					
					// check to see if caching is enabled
					if (properties.isCache())
					{
						value = CacheManager.getInstance().getOrRetieve(clazz, retrieve);
					}
					else
					{
						value = retrieve.retrieve();
					}
				}
			}
		}
		else
		{
			value = null;
		}
		
		return value;
	}
	
	/**
	 * Searches all of the fields of a class and attempts to pick out the fields that return a collection. For each
	 * collection found, this method populates it with test data
	 * 
	 * @param fields
	 */
	@SuppressWarnings("unchecked")
	private void populateCollections(final Object newObject, final Field[] fields)
	{
		// for each of the fields
		for (Field field : fields)
		{
			try
			{
				// make sure this field is directly accessible
				field.setAccessible(true);
				
				// extract the return type from this method
				Class<?> returnType = field.getType();
				
				// check to see if this method is some sort of collection
				if (Collection.class.isAssignableFrom(returnType))
				{
					// get the collection from the object
					Collection<Object> collection = (Collection<Object>) field.get(newObject);
					
					// make sure the collection is not null and that it is not a porxy
					if (null != collection && !Proxy.isProxyClass(collection.getClass()))
					{
						// get the generic type for this collection
						Type type = field.getGenericType();
						
						// make sure there is a generic type assigned for this collection
						if (type instanceof ParameterizedType)
						{
							// attempt to extract the generic from this collection
							ParameterizedType parameterizedTypes = (ParameterizedType) type;
							
							// check the length of the types
							if (1 == parameterizedTypes.getActualTypeArguments().length)
							{
								// ensure that this actualy type argument is actually the instance
								// of a class before
								// attempting to recast it
								if (parameterizedTypes.getActualTypeArguments()[0] instanceof Class)
								{
									final Class<?> clazz = (Class<?>) parameterizedTypes.getActualTypeArguments()[0];
									
									logger.debug("Populating " + collection.getClass().getName()
											+ " with objects of type " + clazz.getName());
									
									// for the specific number of times to auto fill lists
									for (int i = 0; i < properties.getCollectionAutoFillCount(); i++)
									{
										// initialize the new object
										Object object = null;
										
										// create the object that instructs how to retrieve the
										// object
										Retrieve<Object> retrieve = new RetrieveByInitialize(clazz);
										
										// check to see if caching is enabled
										if (properties.isCache())
										{
											object = CacheManager.getInstance().getOrRetieve(clazz, retrieve);
										}
										else
										{
											object = retrieve.retrieve();
										}
										
										// make sure the object is not null
										if (null != object)
										{
											// add the object to the collection
											collection.add(object);
										}
									}
								}
								else
								{
									logger.debug("Could not populate the list of "
											+ parameterizedTypes.getActualTypeArguments()[0].toString()
											+ " because the parameterized type could not be converted to an object.");
								}
							}
						}
					}
				}
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
	
	/**
	 * Checks the {@link properties.getRuleMapping()} to determine if there are any {@link Rule} that match this
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
