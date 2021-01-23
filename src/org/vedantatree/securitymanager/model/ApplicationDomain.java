package org.vedantatree.securitymanager.model;

import java.util.Set;


public class ApplicationDomain
{

	private long					id;
	private String					name;
	private String					description;
	private Set<AppDomainEntity>	appDomainEntities;
	private Object					appSpecificInformation;

	public Object getAppSpecificInformation()
	{
		return appSpecificInformation;
	}

	public void setAppSpecificInformation( Object appSpecificInformation )
	{
		this.appSpecificInformation = appSpecificInformation;
	}

	public Set<AppDomainEntity> getAppDomainEntities()
	{
		return appDomainEntities;
	}

	public void setAppDomainEntities( Set<AppDomainEntity> appDomainEntities )
	{
		this.appDomainEntities = appDomainEntities;
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
