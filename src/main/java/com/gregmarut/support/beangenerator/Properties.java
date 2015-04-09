/*******************************************************************************
 * Copyright (c) 2013 Greg Marut. All rights reserved. This program and the accompanying materials are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.support.beangenerator;

import com.gregmarut.support.beangenerator.rule.RuleMapping;

public class Properties
{
	// holds the default values to use for initializing model objects
	private DefaultValues defaultValues;
	
	// holds the interface mapper for initializing model objects
	private InterfaceMapper interfaceMapper;
	
	// holds the rule mapping for setting custom values on specific fields
	private RuleMapping ruleMapping;
	
	// holds the number of objects to auto populate into a collection whenever
	// one is created
	private int collectionAutoFillCount;
	
	// determines whether or not unmapped interfaces should be proxied
	private boolean proxyUnmappedInterfaces;
	
	// ** Primitives **//
	// determines whether or not beans should be cached
	// once they are initialized
	private boolean cache;
	
	/**
	 * Sets the {@link DefaultValues} to be used when populating test data
	 * 
	 * @param defaultValues
	 */
	public void setDefaultValues(final DefaultValues defaultValues)
	{
		this.defaultValues = defaultValues;
	}
	
	/**
	 * Returns the {@link DefaultValues}
	 * 
	 * @return DefaultValues
	 */
	public DefaultValues getDefaultValues()
	{
		return defaultValues;
	}
	
	/**
	 * Sets the {@link InterfaceMapper} to determine which concrete classes should be instantiated in place of
	 * pre-defined interfaces
	 * 
	 * @param interfaceMapper
	 */
	public void setInterfaceMapper(final InterfaceMapper interfaceMapper)
	{
		this.interfaceMapper = interfaceMapper;
	}
	
	/**
	 * Returns the {@link InterfaceMapper}
	 * 
	 * @return InterfaceMapper
	 */
	public InterfaceMapper getInterfaceMapper()
	{
		return interfaceMapper;
	}
	
	/**
	 * Sets the {@link RuleMapping} object to handle special cases
	 * 
	 * @param ruleMapping
	 */
	public void setRuleMapping(final RuleMapping ruleMapping)
	{
		this.ruleMapping = ruleMapping;
	}
	
	/**
	 * Returns the {@link RuleMapping}
	 * 
	 * @return RuleMapping
	 */
	public RuleMapping getRuleMapping()
	{
		return ruleMapping;
	}
	
	/**
	 * Determines if a cache should be used when creating objects
	 * 
	 * @return boolean
	 */
	public boolean isCache()
	{
		return cache;
	}
	
	public void setCache(boolean cache)
	{
		this.cache = cache;
	}
	
	/**
	 * Sets the number of objects that should be pre-populated into collections whenever one is created
	 * 
	 * @param collectionAutoFillCount
	 */
	public void setCollectionAutoFillCount(int collectionAutoFillCount)
	{
		// make sure the auto fill count is not a negative number
		if (collectionAutoFillCount < 0)
		{
			throw new IllegalArgumentException("collectionAutoFillCount cannot be negative.");
		}
		
		this.collectionAutoFillCount = collectionAutoFillCount;
	}
	
	/**
	 * Returns the number of objects that will be pre-populated into collections
	 * 
	 * @return int
	 */
	public int getCollectionAutoFillCount()
	{
		return collectionAutoFillCount;
	}
	
	public boolean getProxyUnmappedInterfaces()
	{
		return proxyUnmappedInterfaces;
	}
	
	public void setProxyUnmappedInterfaces(boolean proxyUnmappedInterfaces)
	{
		this.proxyUnmappedInterfaces = proxyUnmappedInterfaces;
	}
}
