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
package com.gregmarut.support.beangenerator.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.gregmarut.support.beangenerator.BeanPropertyGenerator;
import com.gregmarut.support.beangenerator.Properties;
import com.gregmarut.support.proxy.GetSetInterfaceProxy;

public class GeneratorInterfaceProxy implements InvocationHandler
{
	// ** Objects **//
	/**
	 * Contains the BeanPropertyGenerator
	 */
	private final BeanPropertyGenerator beanPropertyGenerator;
	
	private GeneratorInterfaceProxy(final BeanPropertyGenerator beanPropertyGenerator)
	{
		this.beanPropertyGenerator = beanPropertyGenerator;
	}
	
	/**
	 * Creates a new proxy class for this handler.
	 * 
	 * @param interfaces
	 * Must be interfaces and not classes
	 * @return New proxy classes
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(final Properties properties, final Class<?>... interfaces)
	{
		// create the new BeanPropertyGenerator with caching enabled
		BeanPropertyGenerator beanPropertyGenerator = new BeanPropertyGenerator(true, true, properties);
		
		// create a new proxy instance
		return (T) Proxy.newProxyInstance(GetSetInterfaceProxy.class.getClassLoader(), interfaces,
			new GeneratorInterfaceProxy(beanPropertyGenerator));
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		return beanPropertyGenerator.get((Class<?>) method.getReturnType());
	}
}
