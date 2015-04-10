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
package com.gregmarut.support.beangenerator;

/**
 * This class is thrown whenever an attempt to initialize a class could not be completed. Once
 * example might be an attempt to instantiate an interface without properly mapping it to a concrete
 * class in {@link InterfaceMapper}.
 * 
 * @author Greg Marut
 */
public final class BeanInitializationException extends RuntimeException
{
	private static final long serialVersionUID = 5260298124435086740L;
	
	public BeanInitializationException()
	{
		
	}
	
	public BeanInitializationException(final Exception cause)
	{
		super(cause);
	}
	
	public BeanInitializationException(final String message)
	{
		super(message);
	}
}
