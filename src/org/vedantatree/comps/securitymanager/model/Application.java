package org.vedantatree.comps.securitymanager.model;

import java.util.Set;


public class Application
{

	private long		id;
	private String		name;
	private Set<Module>	modules;

	public long getId()
	{
		return id;
	}

	public Set<Module> getModules()
	{
		return modules;
	}

	public String getName()
	{
		return name;
	}

	public void setId( long id )
	{
		this.id = id;
	}

	public void setModules( Set<Module> modules )
	{
		this.modules = modules;
	}

	public void setName( String name )
	{
		this.name = name;
	}

}
