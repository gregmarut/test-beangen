package com.gregmarut.support.beangenerator.value;

/**
 * Adds a layer of abstraction for a value allowing for the customization for how a value is retrieved
 * 
 * @author Greg Marut
 * @param <T>
 */
public interface Value<T>
{
	T getValue();
}
