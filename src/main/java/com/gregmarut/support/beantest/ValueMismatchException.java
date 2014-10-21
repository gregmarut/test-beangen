/*******************************************************************************
 * Copyright (c) 2013 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.support.beantest;

public class ValueMismatchException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public ValueMismatchException(final Object expectedValue, final Object actualValue)
	{
		super("Values do not match: Expected \"" + (expectedValue != null ? expectedValue.toString() : "null")
				+ "\", Actual \"" + (actualValue != null ? actualValue.toString() : "null") + "\"");
	}
}
