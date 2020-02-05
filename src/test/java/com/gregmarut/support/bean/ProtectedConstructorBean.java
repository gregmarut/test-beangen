package com.gregmarut.support.bean;

public class ProtectedConstructorBean
{
	private String param1;
	
	private ProtectedConstructorBean()
	{
	
	}
	
	public String getParam1()
	{
		return param1;
	}
	
	public void setParam1(final String param1)
	{
		this.param1 = param1;
	}
}
