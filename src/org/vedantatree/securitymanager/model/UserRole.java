package org.vedantatree.securitymanager.model;

import java.util.Set;


public class UserRole
{

	private int		id;
	private String	description;
	private String	name;
	private Set		menus;

	public String getDescription()
	{
		return description;
	}

	public int getId()
	{
		return id;
	}

	public Set getMenus()
	{
		return menus;
	}

	public String getName()
	{
		return name;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public void setMenus( Set menus )
	{
		this.menus = menus;
	}

	public void setName( String name )
	{
		this.name = name;
	}
}
