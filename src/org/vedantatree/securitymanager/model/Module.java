package org.vedantatree.securitymanager.model;

public class Module
{

	private long		id;
	private String		name;
	private Application	application;

	public Application getApplication()
	{
		return application;
	}

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setApplication( Application application )
	{
		this.application = application;
	}

	public void setId( long id )
	{
		this.id = id;
	}

	public void setName( String name )
	{
		this.name = name;
	}

}
