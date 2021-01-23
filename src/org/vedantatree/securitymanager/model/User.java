package org.vedantatree.securitymanager.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.exceptions.IErrorCodes;
import org.vedantatree.exceptions.SystemException;
import org.vedantatree.utils.StringUtils;
import org.vedantatree.utils.Utilities;


/**
 * Data structure of user object. This represents the User in Security Manager System.
 * 
 * @author Mohit Gupta [mohit.gupta@vedantatree.com]
 */
public class User
{

	private static Log				LOGGER				= LogFactory.getLog( User.class );
	public static int				USER_ACCLOCKED		= 1;
	public static int				USER_INVALID_PWDTKN	= 2;
	public static int				USER_INACTIVE		= 3;
	// User login information
	/**
	 * Id of the user
	 */
	private String					userId;

	/**
	 * Password of the user
	 */
	private String					password;

	// General information for user
	/**
	 * display name of user
	 */
	private String					displayName;

	/**
	 * Title for the user
	 */
	private String					title;

	/**
	 * First name of user
	 */
	private String					firstName;

	/**
	 * Middle name of the user
	 */
	private String					middleName;

	/**
	 * Last name of the user
	 */
	private String					lastName;

	/**
	 * Email id of the user
	 */
	private String					email;

	/**
	 * Phone number of user
	 */
	private String					phone;

	/**
	 * Cell number of user
	 */
	private String					phoneCell;

	/**
	 * Home page URL for user, if any
	 */
	private String					URL;

	/**
	 * Description to describe the user
	 */
	private String					description;

	/**
	 * Available menus for user, which may contains menu items also
	 */
	private Collection<Menu>		menus;

	/**
	 * Global menus for user. Every application has some global menus, which are shown irrespective of page opened.
	 * These are shown generally in top header.
	 */
	private Collection<Menu>		globalMenus;

	/**
	 * Data structure to maintain the mapping of menu url and menu object. It is used to speed up the searching of menus
	 * for a object for given url.
	 */
	private HashMap<String, Menu>	urlToMenuMap;

	/**
	 * User preferences. These are not in use right now, however can help a lot
	 */
	private UserPreferences			preferences;

	private String					entity;

	// TODO need to review the use of this
	private Set						dataEntities;

	/**
	 * map of domain and set of UserRole
	 */
	// TODO need to review the use of domains
	private Map<String, UserRole>	domainRoles;											// ACL acl etc;
	private Map						rights;
	private String					currentDomain		= "default";

	/**
	 * The following properties are required for password change feature
	 */
	private boolean					initialized;
	private boolean					passwordExpired;

	/**
	 * The following three properties are needed for password lock or forgot password handling
	 */
	private boolean					userAccLocked		= false;
	private boolean					isTempPwdSet		= false;

	/**
	 * This contains the application specific information which any application may need to fill to User through
	 * ISecurityService. In that case, any custom information can be carried with User using key/value pair
	 * 
	 * One current scenario is to put list of projects and departments for a user.
	 */
	private Map<Object, Object>		appSpecificInformation;

	public User()
	{
		currentDomain = "default";
		menus = new TreeSet<Menu>();
		globalMenus = new TreeSet<Menu>();
		urlToMenuMap = new HashMap<String, Menu>();
		initialized = false;
	}

	public void initialize( String moduleName )
	{
		if( initialized )
		{
			throw new SystemException( 12, "User is already initialized" );
		}
		else
		{
			initialized = true;
			return;
		}
	}

	public Set getApplicationGroups()
	{
		return dataEntities;
	}

	public String getCurrentDomain()
	{
		return currentDomain;
	}

	public String getEntity()
	{
		return entity;
	}

	public String getDescription()
	{
		return description;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public Map getDomains()
	{
		return domainRoles;
	}

	public String getEmail()
	{
		return email;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public Collection<Menu> getGlobalMenus()
	{
		return globalMenus;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getMiddleName()
	{
		return middleName;
	}

	public String getPassword()
	{
		return password;
	}

	public String getPhone()
	{
		return phone;
	}

	public String getPhoneCell()
	{
		return phoneCell;
	}

	public UserPreferences getPreferences()
	{
		return preferences;
	}

	public String getTitle()
	{
		return title;
	}

	public Collection<Menu> getTopNavigationMenus()
	{
		return menus;
	}

	public String getURL()
	{
		return URL;
	}

	public String getUserId()
	{
		return userId;
	}

	private void setApplicationGroups( Set applicationGroups )
	{
		this.dataEntities = applicationGroups;
	}

	public void setCurrentDomain( String currentDomain )
	{
		this.currentDomain = currentDomain;
	}

	public void setEntity( String entity )
	{
		this.entity = entity;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public void setDisplayName( String displayName )
	{
		this.displayName = displayName;
	}

	private void setDomains( HashMap domains )
	{
		this.domainRoles = domains;
	}

	public void setEmail( String email )
	{
		this.email = email;
	}

	public void setFirstName( String firstName )
	{
		this.firstName = firstName;
	}

	public void setGlobalMenus( Collection<Menu> globalMenus )
	{
		this.globalMenus = globalMenus;
	}

	public void setLastName( String lastName )
	{
		this.lastName = lastName;
	}

	public boolean addMenu( Menu menu )
	{
		Utilities.assertNotNullArgument( menu );
		if( !StringUtils.isQualifiedString( menu.getUrl() ) )
		{
			SystemException se = new SystemException( IErrorCodes.ILLEGAL_STATE_ERROR,
					"URL of menu is found null, it must not be null. menu[" + menu + "]" );
			LOGGER.error( se );
			throw se;
		}
		boolean added = false;
		if( menu.isGlobalMenu() )
		{
			added = globalMenus.add( menu );
		}
		else
		{
			added = menus.add( menu );
		}
		if( added )
		{
			urlToMenuMap.put( menu.getUrl().trim().intern(), menu );
			if( StringUtils.isQualifiedString( menu.getEncryptedURL() ) )
			{
				urlToMenuMap.put( menu.getEncryptedURL().trim().intern(), menu );
			}
		}
		return true;
	}

	public void setMiddleName( String middleName )
	{
		this.middleName = middleName;
	}

	public void setPassword( String password )
	{
		this.password = password;
	}

	public void setPhone( String phone )
	{
		this.phone = phone;
	}

	public void setPhoneCell( String phoneCell )
	{
		this.phoneCell = phoneCell;
	}

	public void setPreferences( UserPreferences preferences )
	{
		this.preferences = preferences;
	}

	public void setTitle( String title )
	{
		this.title = title;
	}

	public void setURL( String url )
	{
		URL = url;
	}

	public void setUserId( String userId )
	{
		this.userId = userId;
	}

	public Map getRights()
	{
		return rights;
	}

	public void setRights( Map rights )
	{
		this.rights = rights;
	}

	private boolean isInRole( String roleName )
	{
		if( domainRoles == null || domainRoles.size() == 0 )
			return false;
		for( Iterator roleIter = getRolesForCurrentDomain().iterator(); roleIter.hasNext(); )
		{
			UserRole userRoles = (UserRole) roleIter.next();
			if( userRoles.getName().equalsIgnoreCase( roleName ) )
				return true;
		}
		return false;
	}

	/**
	 * @return The set of roles for current domain
	 */
	public Set getRolesForCurrentDomain()
	{
		if( domainRoles == null )
		{
			return null;
		}
		return (Set) domainRoles.get( currentDomain );
	}

	/**
	 * @param domain Domain for which user want to retrieve the roles
	 * @return Roles for the specified domain
	 */
	public Set getRolesForDomain( String domain )
	{
		Utilities.assertNotNullArgument( domain );
		return domainRoles != null ? (Set) domainRoles.get( domain ) : null;
	}

	public Collection<MenuItem> getLeftNavigationMenuItems( Menu menu )
	{
		return menu.getLeftNavigationMenuItems();
	}

	public Collection<Menu> getMenus()
	{
		return menus;
	}

	public MenuItem getMenuItemForURL( Menu menu, String URL )
	{
		return menu.getMenuItemForURL( URL );
	}

	public Menu getMenuForURL( String menuURL )
	{
		LOGGER.trace( "getMenuForURL: menuURL[" + menuURL + "]" );

		Utilities.assertNotNullArgument( menuURL );
		if( menus == null || menus.size() == 0 )
		{
			LOGGER.debug( "Returning null as menus found null." );
			return null;
		}
		return urlToMenuMap.get( menuURL.trim() );
		// for( Iterator<Menu> iter = getMergedMenus().iterator(); iter.hasNext(); )
		// {
		// Menu menu = (Menu) iter.next();
		// LOGGER.debug( "merged-menu[" + menu + "]" );
		// if( menu.getUrl().trim().equalsIgnoreCase( menuURL.trim() ) )
		// {
		// LOGGER.debug( "matched menu with simple url. simple-url[" + menuURL + "] menu-found[ " + menu + " ]" );
		// return menu;
		// }
		// if( menu.getEncryptedURL() != null && menu.getEncryptedURL().trim().equalsIgnoreCase( menuURL.trim() ) )
		// {
		// LOGGER.debug( "matched menu for encrypted url. encrypted-url[" + menuURL + "] menu-found[ " + menu
		// + " ]" );
		// return menu;
		// }
		// }
		// return null;
	}

	public Map<Object, Object> getAppSpecificInformation()
	{
		return appSpecificInformation;
	}

	public void setAppSpecificInformation( Map<Object, Object> appSpecificInformation )
	{
		this.appSpecificInformation = appSpecificInformation;
	}

	public Object addAppSepcificInformation( Object identifier, Object value )
	{
		if( appSpecificInformation == null )
		{
			appSpecificInformation = new HashMap<Object, Object>();
		}
		return appSpecificInformation.put( identifier, value );
	}

	public Object getAppSpecificInformation( Object identifier )
	{
		return appSpecificInformation == null ? null : appSpecificInformation.get( identifier );
	}

	public boolean isPasswordExpired()
	{
		return passwordExpired;
	}

	public void setPasswordExpired( boolean passwordExpired )
	{
		this.passwordExpired = passwordExpired;
	}

	public boolean isUserAccLocked()
	{
		return userAccLocked;
	}

	public void setUserAccLocked( boolean userAccLocked )
	{
		this.userAccLocked = userAccLocked;
	}

	public boolean isTempPwdSet()
	{
		return isTempPwdSet;
	}

	public void setTempPwdSet( boolean isTempPwdSet )
	{
		this.isTempPwdSet = isTempPwdSet;
	}

	@Override
	public String toString()
	{
		return "User@" + hashCode() + ": id[" + userId + "] id[" + userId + "] name[" + firstName + " " + lastName
				+ "]";
	}
}
