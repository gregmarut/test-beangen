package com.gregmarut.support.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil
{
	private ReflectionUtil()
	{
		
	}
	
	/**
	 * Retrieve all of the fields for a given object including any parent classes
	 * 
	 * @param object
	 * @return
	 */
	public static Field[] getAllFields(final Object object)
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
	 * Retrieve a method on the given class including any parent class
	 * 
	 * @param object
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getDeclaredMethod(final Object object, final String name, final Class<?>... parameterTypes)
			throws NoSuchMethodException
	{
		// holds the class to inspect
		Class<?> clazz = object.getClass();
		
		// while the class is not null
		while (null != clazz)
		{
			// retrieve the method from this class
			try
			{
				return clazz.getDeclaredMethod(name, parameterTypes);
			}
			catch (NoSuchMethodException e)
			{
				// ignore
			}
			catch (SecurityException e)
			{
				// ignore
			}
			
			// read this class' superclass next if one exists
			clazz = clazz.getSuperclass();
		}
		
		// build the error message
		StringBuilder sb = new StringBuilder();
		sb.append(object.getClass().getName());
		sb.append(".");
		sb.append(name);
		sb.append("()");
		
		throw new NoSuchMethodException(sb.toString());
	}
}
