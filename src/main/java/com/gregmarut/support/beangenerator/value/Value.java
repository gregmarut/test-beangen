package com.gregmarut.support.beangenerator.value;

import java.lang.reflect.Field;

/**
 * Adds a layer of abstraction for a value allowing for the customization for how a value is
 * retrieved
 * 
 * @author Greg Marut
 * @param <T>
 */
public interface Value<T>
{
	/**
	 * Returns the class for this value
	 * 
	 * @return
	 */
	Class<T> getType();
	
	/**
	 * Returns the generated value
	 * 
	 * @return
	 */
	T getValue();
	
	/**
	 * Returns the generated value
	 * 
	 * @param field
	 * the field that this value represents
	 * @return
	 */
	T getValue(Field field);
}
