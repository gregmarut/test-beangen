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
package com.gregmarut.support.bean;

import org.junit.Ignore;

/**
 * A default Bean used for demonstrating tests
 * 
 * @author Greg Marut
 */
@Ignore
public class AnotherTestBean
{
	private Class<?> someClass;
	private String something;
	private String anotherThing;
	private String someID;
	private int someNumber;
	private float someFloat;
	private double someDouble;
	private short someShort;
	
	private int[] someNumbers;
	private byte[] someBytes;
	
	// make sure that the generator can detect and handle cyclical dependencies
	private TestBean testBean;
	
	public String getSomething()
	{
		return something;
	}
	
	public void setSomething(String something)
	{
		this.something = something;
	}
	
	public String getAnotherThing()
	{
		return anotherThing;
	}
	
	public void setAnotherThing(String anotherThing)
	{
		this.anotherThing = anotherThing;
	}
	
	public String getSomeID()
	{
		return someID;
	}
	
	public void setSomeID(String someID)
	{
		this.someID = someID;
	}
	
	public int getSomeNumber()
	{
		return someNumber;
	}
	
	public void setSomeNumber(int someNumber)
	{
		this.someNumber = someNumber;
	}
	
	public float getSomeFloat()
	{
		return someFloat;
	}
	
	public void setSomeFloat(float someFloat)
	{
		this.someFloat = someFloat;
	}
	
	public double getSomeDouble()
	{
		return someDouble;
	}
	
	public void setSomeDouble(double someDouble)
	{
		this.someDouble = someDouble;
	}
	
	public short getSomeShort()
	{
		return someShort;
	}
	
	public int[] getSomeNumbers()
	{
		return someNumbers;
	}
	
	public void setSomeNumbers(int[] someNumbers)
	{
		this.someNumbers = someNumbers;
	}
	
	public byte[] getSomeBytes()
	{
		return someBytes;
	}
	
	public void setSomeBytes(byte[] someBytes)
	{
		this.someBytes = someBytes;
	}
	
	public void setSomeShort(short someShort)
	{
		this.someShort = someShort;
	}
	
	/**
	 * @return the someClass
	 */
	public Class<?> getSomeClass()
	{
		return someClass;
	}
	
	/**
	 * @param someClass
	 *            the someClass to set
	 */
	public void setSomeClass(Class<?> someClass)
	{
		this.someClass = someClass;
	}
	
	/**
	 * @return the testBean
	 */
	public TestBean getTestBean()
	{
		return testBean;
	}
	
	/**
	 * @param testBean
	 *            the testBean to set
	 */
	public void setTestBean(TestBean testBean)
	{
		this.testBean = testBean;
	}
}
