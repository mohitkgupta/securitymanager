package org.vedantatree.securitymanager.model;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.exceptions.IErrorCodes;
import org.vedantatree.exceptions.SystemException;
import org.vedantatree.utils.StringUtils;
import org.vedantatree.utils.Utilities;


/**
 * 
 * @author Mohit Gupta <mohit.gupta@vedantatree.com>
 * 
 */
public class MenuItem implements Comparable<MenuItem>, Cloneable
{

	private static Log				LOGGER	= LogFactory.getLog( MenuItem.class );

	/**
	 * id field
	 */
	private Long					id;

	/**
	 * Index (order) of the menu item in its parent menu
	 */
	private int						index;

	/**
	 * It is the display name of the menu. It can be resource key in I18n arragement
	 */
	private String					displayName;

	/**
	 * This map may contains the display name of Menu based on language
	 */
	private Map<String, String>		languageVsNamesMap;

	/**
	 * Short Cut key for the Menu Item
	 */
	private String					shortCutKey;

	/**
	 * Hot key for the Menu Item
	 */
	private String					hotKey;

	/**
	 * URL of the Menu Item
	 */
	private String					url;

	/**
	 * It is the encrypted URL of the menu. User will see this URL, however internally requested will be forwarded to
	 * actual URL
	 */
	private String					encryptedURL;

	/**
	 * It represents the type of menu item
	 * 
	 * Various possible types are like, left navigation item, rigth navigation item, menu item for list on page, menu
	 * item for form
	 */
	private byte					actionType;

	private String					visibilityCondition;

	/**
	 * It consists the sub menu items
	 */
	private Collection<MenuItem>	subMenuItems;

	private MenuItem				parentMenuItem;

	private Menu					menu;

	public MenuItem()
	{
		subMenuItems = new TreeSet<MenuItem>();
	}

	public MenuItem( int index, String displayName, String url, byte actionType )
	{
		subMenuItems = new TreeSet<MenuItem>();
		this.index = index;
		this.displayName = displayName;
		this.url = url;
		this.actionType = actionType;
	}

	public byte getActionType()
	{
		return actionType;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getHotKey()
	{
		return hotKey;
	}

	public int getIndex()
	{
		return index;
	}

	public String getShortCutKey()
	{
		return shortCutKey;
	}

	public Collection<MenuItem> getSubMenuItems()
	{
		return subMenuItems;
	}

	public String getUrl()
	{
		return url;
	}

	public void setActionType( byte actionType )
	{
		this.actionType = actionType;
	}

	public void setDisplayName( String displayName )
	{
		this.displayName = displayName;
	}

	public void setHotKey( String hotKey )
	{
		this.hotKey = hotKey;
	}

	public void setIndex( int index )
	{
		this.index = index;
	}

	public void setShortCutKey( String shortCutKey )
	{
		this.shortCutKey = shortCutKey;
	}

	private void setSubMenuItems( Collection<MenuItem> subMenuItems )
	{
		this.subMenuItems = subMenuItems;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	public String getVisibilityCondition()
	{
		return visibilityCondition;
	}

	public void setVisibilityCondition( String visibilityCondition )
	{
		this.visibilityCondition = visibilityCondition;
	}

	public Map<String, String> getLanguageVsNamesMap()
	{
		return languageVsNamesMap;
	}

	public void setLanguageVsNamesMap( Map<String, String> languageVsNamesMap )
	{
		this.languageVsNamesMap = languageVsNamesMap;
	}

	public String getEncryptedURL()
	{
		return encryptedURL;
	}

	public void setEncryptedURL( String encryptedURL )
	{
		this.encryptedURL = encryptedURL;
	}

	/**
	 * This url should be used for rendering purpose on web pages. We are trying to return a url which always starts
	 * from Application Context. Like if application root is TestApplication, then complete url always should form like
	 * <contextPath>/menuURL. Here context path will be /TestApplication
	 * 
	 * @return
	 */
	public String getWebPageURL()
	{
		String actionURL = getEncryptedURL();
		// commenting out the code as it is making a url like server:port/menu url instead of
		// server:port/<appcontext>/menu url
		// if this is the behavior, then probably we should keep the application deployment context somewhere to add it
		// before url

		// if( !actionURL.startsWith( "/" ) )
		// {
		// actionURL = "/" + actionURL;
		// }
		return actionURL;
	}

	public MenuItem getParentMenuItem()
	{
		return parentMenuItem;
	}

	private void setParentMenuItem( MenuItem parentMenuItem )
	{
		this.parentMenuItem = parentMenuItem;
	}

	public Menu getMenu()
	{
		return menu;
	}

	void setMenu( Menu menu )
	{
		this.menu = menu;
	}

	public boolean addSubMenuItem( MenuItem subMenuItem )
	{
		Utilities.assertNotNullArgument( subMenuItem );
		if( !StringUtils.isQualifiedString( subMenuItem.getUrl() ) )
		{
			SystemException se = new SystemException( IErrorCodes.ILLEGAL_STATE_ERROR,
					"URL of sub menu item is found null, it must not be null. menu[" + subMenuItem + "]" );
			LOGGER.error( se );
			throw se;
		}
		boolean added = getSubMenuItems().add( subMenuItem );
		if( added )
		{
			subMenuItem.setParentMenuItem( this );
			getMenu().setParentOf( subMenuItem );
		}
		return added;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	public int compareTo( MenuItem menuItem )
	{
		if( menuItem != null )
		{
			// skipping the same items case, as user may leave same index value mistakenly. So we should at least show
			// these items
			return getIndex() >= menuItem.getIndex() ? 1 : -1;
		}
		else
		{
			throw new SystemException( 3,
					"Found null menu item while comparing the menu items. Please specified a valid menu item object" );
		}
	}

	@Override
	public int hashCode()
	{
		int PRIME = 31;
		int result = 1;
		result = PRIME * result + ( displayName != null ? displayName.hashCode() : 0 );
		result = PRIME * result + index;
		result = PRIME * result + ( url != null ? url.hashCode() : 0 );
		result = PRIME * result + ( id != null ? id.hashCode() : 0 );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
		{
			return true;
		}
		if( obj == null )
		{
			return false;
		}
		if( getClass() != obj.getClass() )
		{
			return false;
		}
		MenuItem other = (MenuItem) obj;

		// display name
		if( displayName == null )
		{
			if( other.displayName != null )
			{
				return false;
			}
		}
		else if( !displayName.equals( other.displayName ) )
		{
			return false;
		}

		// index
		if( index != other.index )
		{
			return false;
		}

		// url
		if( url == null )
		{
			if( other.url != null )
			{
				return false;
			}
		}
		else if( !url.equals( other.url ) )
		{
			return false;
		}

		// id
		if( getId() != null )
		{
			if( getId().equals( other.getId() ) )
			{
				return true;
			}
			return false;
		}
		else if( other.getId() != null )
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "MenuItem@" + hashCode() + ": id[" + id + "] index[" + index + "] displayName[" + displayName + "] url["
				+ url + "] actionType[" + actionType + "]";
	}

}

// ---------------------------------------------- Commented Code -------------------------------------------------------

// public MenuItem getSubMenuItemForURL( String menuItemURL )
// {
// // LOGGER.trace( "getSubMenuItemForURL: menuItemURL[" + menuItemURL + "]" );
//
// if( subMenuItems == null )
// {
// LOGGER.debug( "returning null as no sub-items found" );
// return null;
// }
//
// StringUtils.assertQualifiedArgument( menuItemURL );
// menuItemURL = menuItemURL.trim();
//
// MenuItem matchedMenuItem = null;
// for( Iterator<MenuItem> iter = subMenuItems.iterator(); iter.hasNext(); )
// {
// MenuItem menuItem = (MenuItem) iter.next();
// LOGGER.debug( "menuItemURL[" + menuItemURL + "]" );
// if( menuItem.getUrl().trim().equalsIgnoreCase( menuItemURL ) )
// {
// LOGGER.debug( "matched with simple URL[" + menuItem.getUrl() + "]" );
// matchedMenuItem = menuItem;
// break;
// }
// if( menuItem.getEncryptedURL() != null && menuItem.getEncryptedURL().trim().equalsIgnoreCase( menuItemURL ) )
// {
// LOGGER.debug( "matched with encrypted URL[" + menuItem.getEncryptedURL() + "]" );
// matchedMenuItem = menuItem;
// break;
// }
// matchedMenuItem = menuItem.getSubMenuItemForURL( menuItemURL );
// if( matchedMenuItem != null )
// {
// LOGGER.debug( "matched from sub menu item hierarchy" );
// break;
// }
// }
// LOGGER.debug( "returning sub-item[" + matchedMenuItem + "] for URL[" + menuItemURL + "]" );
// return matchedMenuItem;
// }

// public void mergeMenuItem( MenuItem passedMenuItem ) throws CloneNotSupportedException
// {
// if( passedMenuItem != null )
// {
// for( Iterator iter = getSubMenuItems().iterator(); iter.hasNext(); )
// {
// MenuItem currentMenuItem = (MenuItem) iter.next();
// if( currentMenuItem.getUrl().trim().equalsIgnoreCase( passedMenuItem.getUrl().trim() ) )
// currentMenuItem.mergeMenuItem( passedMenuItem );
// else
// passedMenuItem.getSubMenuItems().add( currentMenuItem.clone() );
// }
// }
// else
// {
// throw new SystemException( 3, "MenuItem cannot be null" );
// }
// }
