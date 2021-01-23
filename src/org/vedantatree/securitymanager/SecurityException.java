package org.vedantatree.securitymanager;

import org.vedantatree.exceptions.ApplicationException;


/**
 * This exception is designed to be used by all Security related services.
 * 
 * <p>
 * Currently it is not extending any functionality, but just giving a specific presentation to exception specific to
 * Security Manager. However, in future, it can be extended for any requirements specific to Security Operations.
 * 
 * @author Mohit Gupta [mohit.gupta@vedantatree.com]
 */

public class SecurityException extends ApplicationException
{

	private static final long	serialVersionUID	= 2009012201L;

	public SecurityException( int errorCode, String message )
	{
		super( errorCode, message );
	}

	public SecurityException( int errorCode, String message, Throwable th )
	{
		super( errorCode, message, th );
	}

}
