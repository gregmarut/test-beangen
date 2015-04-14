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
package com.gregmarut.support.beantest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gregmarut.support.beangenerator.BeanPropertyGenerator;

/**
 * Automates the testing of getter and setter methods using reflection
 * 
 * @author Greg Marut
 */
public class GetterSetterTester
{
	public static final String SETTER_PREFIX = "set";
	public static final String GETTER_PREFIX = "get";
	public static final String BOOLEAN_GETTER_PREFIX = "is";
	
	// instantiate the logger
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// holds the bean property generator
	private final BeanPropertyGenerator beanPropertyGenerator;
	
	public GetterSetterTester()
	{
		this(new BeanPropertyGenerator(false, true));
	}
	
	public GetterSetterTester(final BeanPropertyGenerator beanPropertyGenerator)
	{
		this.beanPropertyGenerator = beanPropertyGenerator;
	}
	
	/**
	 * Execute the GetterSetter tests on the target class
	 */
	public void execute(final Class<?>... targetClasses)
	{
		// make sure the classes is not null
		if (null != targetClasses)
		{
			// for each of the target classes
			for (Class<?> targetClass : targetClasses)
			{
				// generate an instance of the target
				Object target = beanPropertyGenerator.get(targetClass);
				
				// execute the test on the target object
				executeObject(target);
			}
		}
		else
		{
			throw new IllegalArgumentException("Target classes cannot be null.");
		}
	}
	
	/**
	 * Execute the GetterSetter tests on the target object
	 */
	public void executeObject(final Object target)
	{
		// make sure the target is not null
		if (null != target)
		{
			// get the list of declared fields from the target object
			Field[] fields = target.getClass().getDeclaredFields();
			
			// for each of the fields
			for (Field field : fields)
			{
				try
				{
					// make sure this field is accessible
					field.setAccessible(true);
					
					// retrieve the getter and setter methods for this field
					Method getterMethod = findGetterMethod(target, field);
					Method setterMethod = findSetterMethod(target, field);
					
					// make sure the getter and setter methods are not null
					if (null != getterMethod && null != setterMethod)
					{
						// instantiate a new value for this field
						Object expectedValue = beanPropertyGenerator.get(field.getType(), false);
						
						// invoke the setter method
						setterMethod.setAccessible(true);
						setterMethod.invoke(target, expectedValue);
						
						// now retrieve the value by invoking the getter method
						getterMethod.setAccessible(true);
						Object actualValue = getterMethod.invoke(target);
						
						// make sure the value is not null
						if (actualValue != null)
						{
							// check to see if the values do not match
							if (!actualValue.equals(expectedValue))
							{
								throw new ValueMismatchException(expectedValue, actualValue);
							}
						}
						else
						{
							// check to see if the expected value is not null
							if (expectedValue != null)
							{
								throw new ValueMismatchException(expectedValue, actualValue);
							}
						}
					}
				}
				catch (NoSuchMethodException e)
				{
					logger.info(e.getMessage(), e);
				}
				catch (IllegalArgumentException e)
				{
					logger.info(e.getMessage(), e);
				}
				catch (IllegalAccessException e)
				{
					logger.info(e.getMessage(), e);
				}
				catch (InvocationTargetException e)
				{
					logger.info(e.getMessage(), e);
				}
			}
		}
		else
		{
			throw new IllegalArgumentException("Target cannot be null.");
		}
	}
	
	/**
	 * Searches the target object looking for the getter method that would match the field
	 * 
	 * @param target
	 * @param field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private Method findGetterMethod(final Object target, final Field field) throws SecurityException,
			NoSuchMethodException
	{
		// create the getter field name
		String getterFieldName;
		
		// check to see if this is a boolean
		if (field.getType().equals(Boolean.class))
		{
			getterFieldName = convertFieldToMethod(BOOLEAN_GETTER_PREFIX, field.getName());
		}
		else
		{
			getterFieldName = convertFieldToMethod(GETTER_PREFIX, field.getName());
		}
		
		// find this method
		return target.getClass().getDeclaredMethod(getterFieldName);
	}
	
	/**
	 * Searches the target object looking for the setter method that would match the field
	 * 
	 * @param target
	 * @param field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private Method findSetterMethod(final Object target, final Field field) throws SecurityException,
			NoSuchMethodException
	{
		// create the setter field name
		String setterFieldName = convertFieldToMethod(SETTER_PREFIX, field.getName());
		
		// find this method
		return target.getClass().getDeclaredMethod(setterFieldName, field.getType());
	}
	
	/**
	 * Converts a field name into a representation of the getter/setter method
	 * 
	 * @param prefix
	 * @param fieldName
	 * @return
	 */
	private String convertFieldToMethod(final String prefix, final String fieldName)
	{
		// convert the field name to the appropriate method
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(fieldName.substring(0, 1).toUpperCase());
		
		if (fieldName.length() > 1)
		{
			sb.append(fieldName.substring(1));
		}
		
		return sb.toString();
	}
	
	public BeanPropertyGenerator getBeanPropertyGenerator()
	{
		return beanPropertyGenerator;
	}
}
