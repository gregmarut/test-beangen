package com.gregmarut.support.beangenerator.model;

import java.lang.reflect.Field;

/**
 * Represents a class that holds a java field object and the object in which it belongs to
 * 
 * @author Greg Marut
 */
public class FieldMember
{
	private final Field field;
	private final Object declaringObject;
	
	public FieldMember(final Field field, final Object declaringObject)
	{
		this.field = field;
		this.declaringObject = declaringObject;
	}
	
	public Field getField()
	{
		return field;
	}
	
	public Object getDeclaringObject()
	{
		return declaringObject;
	}
}
