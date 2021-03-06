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
package com.gregmarut.support.bean;

import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A default Bean used for demonstrating tests
 *
 * @author Greg Marut
 */
@Ignore
public class TestBean
{
	private final List<AnotherTestBean> finalList;
	private List<AnotherTestBean> list;
	
	private String firstName;
	private String lastName;
	private String accountID;
	private String dateOfBirth;
	private Map<?, ?> map;
	private Integer integer;
	private AnotherTestBean anotherTestBean;
	
	private float floatData;
	private int intData;
	
	public TestBean()
	{
		this.finalList = new ArrayList<AnotherTestBean>();
	}
	
	public List<AnotherTestBean> getFinalList()
	{
		return finalList;
	}
	
	public List<AnotherTestBean> getList()
	{
		return list;
	}
	
	public void setList(final List<AnotherTestBean> list)
	{
		this.list = list;
	}
	
	public float getFloatData()
	{
		return floatData;
	}
	
	public void setFloatData(float floatData)
	{
		this.floatData = floatData;
	}
	
	public int getIntData()
	{
		return intData;
	}
	
	public void setIntData(int intData)
	{
		this.intData = intData;
	}
	
	public Integer getInteger()
	{
		return integer;
	}
	
	public void setInteger(Integer integer)
	{
		this.integer = integer;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getAccountID()
	{
		return accountID;
	}
	
	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}
	
	public void setMap(Map<?, ?> map)
	{
		this.map = map;
	}
	
	public Map<?, ?> getMap()
	{
		return map;
	}
	
	public void setAnotherTestBean(AnotherTestBean anotherTestBean)
	{
		this.anotherTestBean = anotherTestBean;
	}
	
	public AnotherTestBean getAnotherTestBean()
	{
		return anotherTestBean;
	}
	
	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}
}
