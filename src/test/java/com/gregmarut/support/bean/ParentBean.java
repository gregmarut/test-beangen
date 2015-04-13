package com.gregmarut.support.bean;

public class ParentBean extends GrandParentBean
{
	private String someParentString;
	private String anotherParentString;
	
	public String getSomeParentString()
	{
		return someParentString;
	}
	
	public void setSomeParentString(String someParentString)
	{
		this.someParentString = someParentString;
	}
	
	public String getAnotherParentString()
	{
		return anotherParentString;
	}
	
	public void setAnotherParentString(String anotherParentString)
	{
		this.anotherParentString = anotherParentString;
	}
}
