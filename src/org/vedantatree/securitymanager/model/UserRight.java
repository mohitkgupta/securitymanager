package org.vedantatree.securitymanager.model;

import java.io.Serializable;


/**
 * Object of this class represents the Rights any user has for the application.
 * 
 * It is not in use currently with our framework, however can be used at some time later with some modification
 * 
 * 
 * @author Mohit Gupta <mohit.gupta@vedantatree.com>
 * 
 */
public class UserRight implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5910608268971887800L;
	private String				right				= null;
	private String				jstlCondition		= null;
	private String				sqlCondition		= null;

	public UserRight()
	{
	}

	public boolean equals( Object o )
	{
		return ( o instanceof UserRight ) ? right.equals( ( (UserRight) o ).getRight() ) : false;
	}

	public int hashCode()
	{
		return right.hashCode();
	}

	public String getRight()
	{
		return right;
	}

	public void setRight( String right )
	{
		this.right = right;
	}

	public String getJstlCondition()
	{
		return jstlCondition;
	}

	public void setJstlCondition( String jstlCondition )
	{
		this.jstlCondition = jstlCondition;
	}

	public String getSqlCondition()
	{
		return sqlCondition;
	}

	public void setSqlCondition( String sqlCondition )
	{
		this.sqlCondition = sqlCondition;
	}

	public String toString()
	{
		return "right=" + right + ", jstlCondition=" + jstlCondition;
	}

}
