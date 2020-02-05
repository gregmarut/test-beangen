package com.gregmarut.support.bean;

public class PrivateConstructorBean
{
	private String param1;
	
	private PrivateConstructorBean()
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
