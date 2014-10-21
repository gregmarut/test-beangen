/*******************************************************************************
 * Copyright (c) 2013 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.support.beangenerator;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gregmarut.support.beangenerator.proxy.GeneratorInterfaceProxy;
import com.gregmarut.support.beangenerator.rule.Rule;

/**
 * This class is responsible for the actual initialization of a bean object. It uses reflection to
 * cascade an object
 * and populate its fields
 * 
 * @author Greg Marut
 */
public abstract class BeanPropertyInitializer
{
	// ** Objects **//
	// instantiate the logger
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Properties properties;
	
	// holds the stack of instantiated classes to detect and prevent infinite loops
	protected final Stack<Class<?>> instantiationStack;
	
	/**
	 * Constructs a new BeanPropertyInitializer
	 * 
	 * @param properties
	 */
	BeanPropertyInitializer(final Properties properties)
	{
		setProperties(properties);
		
		instantiationStack = new Stack<Class<?>>();
	}
	
	/**
	 * Initializes a class and returns a new instantiated object. All fields in the new object are
	 * also instantiated.
	 * 
	 * @param clazz
	 * @return Object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	final Object initialize(final Class<?> clazz) throws InstantiationException, IllegalAccessException
	{
		return initialize(clazz, true);
	}
	
	/**
	 * Initializes a class and returns a new instantiated object. All fields in the new object are
	 * also instantiated
	 * provided the populate boolean is set to true.
	 * 
	 * @param clazz
	 * @param populate
	 * @return Object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	final Object initialize(final Class<?> clazz, final boolean populate) throws InstantiationException,
		IllegalAccessException
	{
		logger.debug("Initializing ", clazz.getName());
		
		// instantiate a new version of this method
		Object newObject = instantiate(clazz);
		
		// make sure this class does not already exist in the instantiation stack
		if (!instantiationStack.contains(clazz))
		{
			// make sure the new object is not null
			// a new object can only be null if it was specifically defined as null in the
			// properties.getDefaultValues()
			if (null != newObject && !Proxy.isProxyClass(newObject.getClass()))
			{
				// push this class onto the stack
				instantiationStack.push(clazz);
				
				if (populate)
				{
					// populate the object via methods
					populate(newObject);
				}
				
				// remove this class from the stack
				instantiationStack.pop();
			}
		}
		else
		{
			// an infinite loop was detected
			logger.info("Cyclical dependency detected while attempting to initialize " + clazz.getName()
				+ ". Skipping object population.");
		}
		
		// return the new object
		return newObject;
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
		// a new object can only be null if it was specifically defined as null in the
		// properties.getDefaultValues()
		if (null != object)
		{
			logger.debug("Initializing " + object.getClass().getName());
			
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
	
	protected abstract void populate(final Object object);
	
	/**
	 * Instantiates a new instance of the class. If the class is an interface, this method will
	 * attempt to lookup the
	 * corresponding concrete class in the {@link InterfaceMapper}.
	 * 
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected final Object instantiate(final Class<?> clazz) throws InstantiationException, IllegalAccessException
	{
		// holds the object to return
		Object newObject;
		
		// check to see if this class is an interface
		if (clazz.isInterface())
		{
			// attempt to map the interface to a concrete class to instantiate instead
			Class<?> concreteClass = properties.getInterfaceMapper().get(clazz);
			
			if (null != concreteClass)
			{
				logger.debug(concreteClass.getName() + " found to replace interface " + clazz.getName());
				
				// instantiate a new instance of the concrete class
				newObject = initialize(concreteClass);
			}
			else
			{
				// check to see if proxies should be generator for unmapped interfaces
				if (properties.getProxyUnmappedInterfaces())
				{
					// create a new proxy for this interface
					newObject = GeneratorInterfaceProxy.createProxy(properties, clazz);
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
				newObject = enumValues[0];
			}
			else
			{
				newObject = null;
			}
		}
		else
		{
			// check to see if this value exists in the default values map
			if (properties.getDefaultValues().containsKey(clazz))
			{
				logger.debug("Found default value for " + clazz.getName());
				
				newObject = properties.getDefaultValues().get(clazz);
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
	 * Checks the {@link properties.getRuleMapping()} to determine if there are any {@link Rule}
	 * that match this
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
		if (null != properties.getRuleMapping())
		{
			// convert the class from its primitive value if applicable, otherwise use the original
			// value
			// The reason that primitives have to be cast up is because Generics does not support
			// primitives.
			Class<?> nonPrimitiveClass = ClassConversionUtil.convertToNonPrimitive(clazz);
			
			// check to see if the rule mapping contains rules for this parameter type
			if (properties.getRuleMapping().contains(nonPrimitiveClass))
			{
				// get the list of rules from the rule mapping based on this parameter type
				List<Rule<?>> rules = properties.getRuleMapping().get(nonPrimitiveClass);
				
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
	
	public final void setProperties(final Properties properties)
	{
		if (null == properties)
		{
			throw new IllegalArgumentException("properties cannot be null.");
		}
		
		this.properties = properties;
	}
	
	public final Properties getProperties()
	{
		return properties;
	}
}
