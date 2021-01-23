package org.vedantatree.securitymanager.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.exceptions.IErrorCodes;
import org.vedantatree.exceptions.SystemException;
import org.vedantatree.utils.StringUtils;
import org.vedantatree.utils.Utilities;


/**
 * @author Mohit Gupta <mohit.gupta@vedantatree.com>
 */
public class Menu implements Comparable<Menu>
{

	private static Log				LOGGER				= LogFactory.getLog( Menu.class );

	public static final String		GLOBAL_MODULE_MENU	= "global";
	// ====== Action Types
	public static final byte		LEFT_NAV_ACTION		= 1;
	public static final byte		RIGHT_NAV_ACTION	= 5;
	public static final byte		PAGE_ACTION			= 2;
	public static final byte		LIST_ACTION			= 3;
	public static final byte		LIST_HEADER_ACTION	= 4;

	/**
	 * id of the menu
	 */
	private Long					id;

	/**
	 * Index of the Menu - It is used for ordering
	 */
	private int						index;

	/**
	 * It is the display name of the Menu
	 */
	private String					displayName;

	/**
	 * This map may contains the display name of Menu based on language
	 */
	private Map<String, String>		languageVsNamesMap;

	/**
	 * It is the short cut key to operate the menu
	 */
	private String					shortCutKey;

	/**
	 * Hot key for the menu
	 */
	private String					hotKey;

	/**
	 * URL of the Menu, given in Supervision module
	 */
	private String					url;

	/**
	 * It is the encrypted URL of the menu. User will see this URL, however internally requested will be forwarded to
	 * actual URL
	 */
	private String					encryptedURL;

	/**
	 * Module name for which this menu is assigned
	 */
	private String					moduleName;

	/**
	 * TODO it is not in use right now. It may need to be removed
	 */
	private String					pageName;

	/**
	 * All the menu items for the Menu. It includes left navigation menu items also
	 */
	private Collection<MenuItem>	menuItems;

	/**
	 * It is the map of children menuItem's url and menuItems. Point to consider, it contains all children menuItems in
	 * the hierarchy. It is specially designed for making search fast for menu items. The menu Items are searched on
	 * every request to security manager for any URL
	 */
	private Map<String, MenuItem>	urlToMenuItemMap;

	/**
	 * Collection of Left navigation menu items only
	 */
	private Collection<MenuItem>	leftNavigationMenuItems;

	public Menu()
	{
		menuItems = new TreeSet<>();
		leftNavigationMenuItems = new TreeSet<>();
		urlToMenuItemMap = new HashMap<>();
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getHotKey()
	{
		return hotKey;
	}

	public Long getId()
	{
		return id;
	}

	public int getIndex()
	{
		return index;
	}

	public Collection<MenuItem> getLeftNavigationMenuItems()
	{
		return leftNavigationMenuItems;
	}

	public Collection<MenuItem> getMenuItems()
	{
		return menuItems;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public String getPageName()
	{
		return pageName;
	}

	public String getShortCutKey()
	{
		return shortCutKey;
	}

	public String getUrl()
	{
		return url;
	}

	public boolean isGlobalMenu()
	{
		return moduleName != null && moduleName.trim().equalsIgnoreCase( GLOBAL_MODULE_MENU );
	}

	public void setDisplayName( String displayName )
	{
		this.displayName = displayName;
	}

	public void setHotKey( String hotKey )
	{
		this.hotKey = hotKey;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	public void setIndex( int index )
	{
		this.index = index;
	}

	private void setMenuItems( Collection<MenuItem> menuItems )
	{
		this.menuItems = menuItems;
	}

	public void setModuleName( String moduleName )
	{
		this.moduleName = moduleName;
	}

	public void setPageName( String pageName )
	{
		this.pageName = pageName;
	}

	public void setShortCutKey( String shortCutKey )
	{
		this.shortCutKey = shortCutKey;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public String getEncryptedURL()
	{
		return encryptedURL;
	}

	public void setEncryptedURL( String encryptedURL )
	{
		this.encryptedURL = encryptedURL;
	}

	public Map<String, String> getLanguageVsNamesMap()
	{
		return languageVsNamesMap;
	}

	public void setLanguageVsNamesMap( Map<String, String> languageVsNamesMap )
	{
		this.languageVsNamesMap = languageVsNamesMap;
	}

	/**
	 * It checks whether this menu contains any menuItem for specified URL
	 * 
	 * @param URL url of MenuItem for which we need to search for the MenuItem
	 * @return true if MenuItem exists, false otherwise
	 */
	public boolean containsMenuItemForURL( String URL )
	{
		return getMenuItemForURL( URL ) != null;
	}

	/**
	 * It returns a menu item for specified URL, if it exists
	 * 
	 * This method not only returns the immediate children of this menu, but also will take care of nested children in
	 * hierarchy
	 * 
	 * @param menuItemURL url of the menu item
	 * @return Matching MenuItem if found, null otherwise
	 */
	public MenuItem getMenuItemForURL( String menuItemURL )
	{
		LOGGER.trace( "getMenuItemForURL: menuItemURL[" + menuItemURL + "]" );
		StringUtils.assertQualifiedArgument( menuItemURL );
		menuItemURL = menuItemURL.trim();

		// urlToMenuItemMap contains all children menuItems even from Hierarchy
		MenuItem matchedMenuItem = urlToMenuItemMap.get( menuItemURL );
		LOGGER.debug( "returning menu[" + matchedMenuItem + "] for url[" + menuItemURL + "]" );

		return matchedMenuItem;

		// for( Iterator<MenuItem> iter = getMenuItems().iterator(); iter.hasNext(); )
		// {
		// MenuItem menuItem = (MenuItem) iter.next();
		// LOGGER.debug( "iter-menuItem[" + menuItem + "]" );
		//
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

		// return matchedMenuItem;
	}

	// internal method called from clone only
	private Collection<MenuItem> addChildrenToMenuItems( Collection<MenuItem> childMenuItems,
			Collection<MenuItem> menuItems )
	{
		Iterator<MenuItem> iterator = childMenuItems.iterator();
		do
		{
			if( !iterator.hasNext() )
				break;
			MenuItem menuItemValue = iterator.next();
			if( menuItemValue != null )
				menuItems.add( menuItemValue );
			if( menuItemValue.getSubMenuItems() != null && menuItemValue.getSubMenuItems().size() > 0 )
				addChildrenToMenuItems( menuItemValue.getSubMenuItems(), menuItems );
		} while( true );
		return menuItems;
	}

	/**
	 * It is used to add a menu item as left navigation item. Any item coming to this method is taken as left navigation
	 * item. So we assume that caller will check the action type
	 * 
	 * @param leftNavigationMenuItem MenuItem to add as left navigation item
	 * @return
	 */
	private Collection<MenuItem> addLeftNavigationMenuItems( MenuItem leftNavigationMenuItem )
	{
		if( leftNavigationMenuItem != null )
		{
			getLeftNavigationMenuItems().add( leftNavigationMenuItem );
			getMenuItems().add( leftNavigationMenuItem );
			return addChildrenToMenuItems( leftNavigationMenuItem.getSubMenuItems(), getMenuItems() );
		}
		else
		{
			throw new SystemException( 3, "MenuItem cannot be null" );
		}
	}

	public boolean addMenuItem( MenuItem menuItem )
	{
		Utilities.assertNotNullArgument( menuItem );
		if( !StringUtils.isQualifiedString( menuItem.getUrl() ) )
		{
			SystemException se = new SystemException( IErrorCodes.ILLEGAL_STATE_ERROR,
					"URL of menu item is found null, it must not be null. menu[" + menuItem + "]" );
			LOGGER.error( se );
			throw se;
		}
		boolean added = getMenuItems().add( menuItem );
		if( added )
		{
			if( menuItem.getActionType() == LEFT_NAV_ACTION )
			{
				getLeftNavigationMenuItems().add( menuItem );
			}
			setParentOf( menuItem );
		}
		return added;
	}

	void setParentOf( MenuItem menuItem )
	{
		menuItem.setMenu( this );
		addToURLToMenuItemMap( menuItem );
	}

	private void addToURLToMenuItemMap( MenuItem menuItem )
	{
		urlToMenuItemMap.put( menuItem.getUrl().trim().intern(), menuItem );
		if( StringUtils.isQualifiedString( menuItem.getEncryptedURL() ) )
		{
			urlToMenuItemMap.put( menuItem.getEncryptedURL().trim().intern(), menuItem );
		}
	}

	private boolean addMenuItem( MenuItem menuItemToAdd, Collection<MenuItem> menuItems )
	{
		if( menuItemToAdd != null )
		{
			for( MenuItem menuItem : menuItems )
			{
				MenuItem currentMenuItem = menuItem;
				if( currentMenuItem.getUrl().trim().equalsIgnoreCase( menuItemToAdd.getUrl().trim() ) )
					return false;
			}

			getMenuItems().add( menuItemToAdd );
			return true;
		}
		else
		{
			throw new SystemException( 3, "MenuItem cannot be null" );
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Menu clonedMenu = (Menu) super.clone();
		Collection<MenuItem> menuItems = clonedMenu.getMenuItems();
		if( menuItems != null && menuItems.size() > 0 )
		{
			MenuItem menuItem;
			for( Iterator<MenuItem> iter = menuItems.iterator(); iter.hasNext(); clonedMenu
					.addMenuItem( (MenuItem) menuItem.clone(), menuItems ) )
				menuItem = iter.next();

		}
		Collection<MenuItem> leftNavMenuItems = clonedMenu.getLeftNavigationMenuItems();
		if( leftNavMenuItems != null && leftNavMenuItems.size() > 0 )
		{
			MenuItem menuItem;
			for( Iterator<MenuItem> iter = leftNavMenuItems.iterator(); iter.hasNext(); clonedMenu
					.addLeftNavigationMenuItems( (MenuItem) menuItem.clone() ) )
				menuItem = iter.next();

		}
		return clonedMenu;
	}

	@Override
	public int compareTo( Menu menuToCompare )
	{
		if( menuToCompare != null && ( menuToCompare instanceof Menu ) )
		{
			Menu menu = menuToCompare;
			// skipping the same items case, as user may leave same index value mistakenly. So we should at least show
			// these items
			return getIndex() >= menu.getIndex() ? 1 : -1;
		}
		else
		{
			throw new SystemException( 3, "Menu can not be null.Please set the menu" );
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
		Menu other = (Menu) obj;

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
		return "Menu@" + hashCode() + ": id[" + id + "] index[" + index + "] displayName[" + displayName + "] url["
				+ url + "] pageName[" + pageName + "] moduleName[" + moduleName + "]";
	}

}

// ---------------------------------------- Commenetd Code - dont delete it for now ---------------------------------

// not in use - can be removed
// public void mergeMenu( Menu passedMenu ) throws CloneNotSupportedException
// {
// Collection newMenuItems = new HashSet();
// Iterator iter = getMenuItems().iterator();
// do
// {
// if( !iter.hasNext() )
// break;
// MenuItem currentMenuItem = (MenuItem) iter.next();
// Iterator iterator = passedMenu.getMenuItems().iterator();
// do
// {
// if( !iterator.hasNext() )
// break;
// MenuItem passedMenuItem = (MenuItem) iterator.next();
// if( !currentMenuItem.getUrl().trim().equalsIgnoreCase( passedMenuItem.getUrl().trim() ) )
// newMenuItems.add( currentMenuItem.clone() );
// } while( true );
// if( newMenuItems.size() > 0 )
// passedMenu.getMenuItems().addAll( newMenuItems );
// } while( true );
// newMenuItems.clear();
// for( iter = getLeftNavigationMenuItems().iterator(); iter.hasNext(); )
// {
// MenuItem currentLeftNavMenuItem = (MenuItem) iter.next();
// Iterator iterator = passedMenu.getLeftNavigationMenuItems().iterator();
// while( iterator.hasNext() )
// {
// MenuItem passedLeftNavMenuItem = (MenuItem) iterator.next();
// if( !currentLeftNavMenuItem.getUrl().trim().equalsIgnoreCase( passedLeftNavMenuItem.getUrl().trim() ) )
// newMenuItems.add( currentLeftNavMenuItem.clone() );
// else
// currentLeftNavMenuItem.mergeMenuItem( passedLeftNavMenuItem );
// }
// }
//
// if( newMenuItems.size() > 0 )
// passedMenu.getLeftNavigationMenuItems().addAll( newMenuItems );
// }
