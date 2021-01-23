package org.vedantatree.comps.securitymanager.model;

public class AppDomainEntity
{

	private long				id;
	private String				name;
	private String				description;
	private ApplicationDomain	applicationDomain;

	public ApplicationDomain getApplicationDomain()
	{
		return applicationDomain;
	}

	public String getDescription()
	{
		return description;
	}

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setApplicationDomain( ApplicationDomain applicationDomain )
	{
		this.applicationDomain = applicationDomain;
	}

	public void setDescription( String description )
	{
		this.description = description;
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
