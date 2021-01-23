package org.vedantatree.securitymanager.model;

public class UserPreferences
{

	private String	tiles;
	private String	styleSheet;
	private String	locale;

	public String getLocale()
	{
		return locale;
	}

	public String getStyleSheet()
	{
		return styleSheet;
	}

	public String getTiles()
	{
		return tiles;
	}

	public void setLocale( String locale )
	{
		this.locale = locale;
	}

	public void setStyleSheet( String styleSheet )
	{
		this.styleSheet = styleSheet;
	}

	public void setTiles( String tiles )
	{
		this.tiles = tiles;
	}

}
