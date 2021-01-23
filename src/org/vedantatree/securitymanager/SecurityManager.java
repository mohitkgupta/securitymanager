package org.vedantatree.securitymanager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.config.ConfigurationManager;
import org.vedantatree.exceptions.ApplicationException;
import org.vedantatree.exceptions.ExceptionUtils;
import org.vedantatree.exceptions.IErrorCodes;
import org.vedantatree.exceptions.SystemException;
import org.vedantatree.securitymanager.model.ApplicationDomain;
import org.vedantatree.securitymanager.model.Menu;
import org.vedantatree.securitymanager.model.MenuItem;
import org.vedantatree.securitymanager.model.User;
import org.vedantatree.utils.BeanUtils;
import org.vedantatree.utils.StringUtils;
import org.vedantatree.utils.Utilities;


/**
 * This component provides the security for whole Application at the level of Authentication and Authorization. It also
 * provides the right menus and menu items for a user as per her roles in application.
 * 
 * Internally it uses Security Service object to get the services. Security service is a plugable component to it.
 * 
 * @author Mohit Gupta <mohit.gupta@vedantatree.com>
 */
public class SecurityManager
{

	private static Log				LOGGER						= LogFactory.getLog( SecurityManager.class );

	public static final String		SECURITY_MANAGER_CLASSNAME	= SecurityManager.class.getName() + "_CurrentSM";

	private static SecurityManager	sharedInstance;
	private ISecurityService		securityService;

	/**
	 * It returns the shared security manager instance. Still it provides the facility to create the shared instance of
	 * one of extended class of Security Manager. Developer can specify the fully qualified class name of extended
	 * Security Manager in property file.
	 * 
	 * @return Shared instance of Security Manager
	 */
	public static SecurityManager getSharedInstance()
	{
		if( sharedInstance == null )
		{
			String securityManagerClassName = ConfigurationManager.getSharedInstance()
					.getPropertyValue( SECURITY_MANAGER_CLASSNAME );
			Object newInstance;

			if( securityManagerClassName.equals( "com.daffodil.comps.securitymanager.SecurityManager" ) )
			{
				sharedInstance = new SecurityManager();
			}
			else
			{
				try
				{
					newInstance = BeanUtils.newInstance( securityManagerClassName, null, null );
				}
				catch( ApplicationException e )
				{
					SystemException se = new SystemException( e.getErrorCode(), e.getMessage(), e );
					LOGGER.error( se );
					throw se;
				}
				if( !( newInstance instanceof SecurityManager ) )
				{
					SystemException se = new SystemException( IErrorCodes.ILLEGAL_ARGUMENT_ERROR,
							"Wrong security manager class specified. It is not of security manager type. specifiedClassName["
									+ securityManagerClassName + "]" );
					LOGGER.error( se );
					throw se;
				}
				sharedInstance = (SecurityManager) newInstance;
			}
		}
		return sharedInstance;
	}

	protected SecurityManager()
	{
		// initial security service which implements ISecurityService interface, and can provide all security data
		// objects
		// data objects can come from any database, file or remote bean whatever
		// this.securityService = initializeSecurityService();
	}

	public final ISecurityService getSecurityService()
	{
		return securityService;
	}

	/**
	 * It authenticates the user credential and return the User object after authentication.
	 * 
	 * @param userName id of the user
	 * @param password Password of the user
	 * @param applicationName Name of the application, where we need to check the authenticity of specified credentials
	 * @return User object, if information is authenticated
	 * @throws SecurityException If any problem exists, like information is not authenticated
	 */
	public final User authenticate( String userName, String password, String applicationName ) throws SecurityException
	{
		LOGGER.trace( "authenticate: userName[" + userName + "] password["
				+ ( password != null && password.trim().length() > 0 ) + "] appName[" + applicationName + "]" );
		StringUtils.assertQualifiedArgument( userName );
		StringUtils.assertQualifiedArgument( password );
		User user = retrieveAndVerifyUser( userName, password, applicationName );
		return user;
	}

	protected User retrieveAndVerifyUser( String userName, String userPassword, String applicationName )
			throws SecurityException
	{
		LOGGER.trace( "retrieveAndVerifyUser: userName[ " + userName + " ]" );

		User user = null;
		/**
		 * Supervision Delegate is called for Supervision
		 */
		try
		{
			user = getSecurityService().getUser( userName, userPassword, applicationName );
		}
		catch( ApplicationException ae )
		{
			ExceptionUtils.logException( LOGGER, ae.getMessage(), ae );
			throw new SecurityException( ae.getErrorCode(), ae.getMessage() );
		}
		return user;
	}

	/**
	 * This method is used for getting Valid Menu if user have rights on specified url.
	 * 
	 * @param user User object for which we need to check the rights on given url
	 * @param menuURL menu url to check for rights
	 * @return Menu object of menu if user has rights on it
	 * @throws SecurityException Seuciryt exception if user does not have right, or if any other problem occurs
	 */
	public Menu validateRequestForMenu( User user, String menuURL ) throws SecurityException
	{
		LOGGER.trace( "validateRequestForMenu: user[" + user + "] URL[" + menuURL + "]" );

		StringUtils.assertQualifiedArgument( menuURL );
		if( user == null )
		{
			SecurityException se = new SecurityException( IErrorCodes.AUTHENTICATION_FAILURE,
					"No User found. User is either not authorized or not logged in" );
			ExceptionUtils.logException( LOGGER, null, se );
			throw se;
		}

		Menu menu = user.getMenuForURL( menuURL );
		LOGGER.debug( "retrieved menu[" + menu + "]" );
		if( menu != null )
		{
			return menu;
		}

		SecurityException se = new SecurityException( IErrorCodes.RESOURCE_NOT_FOUND,
				"No menu found for specified URL[" + menuURL + "]" );
		LOGGER.info( se.getMessage() );
		throw se;
	}

	/**
	 * This method is check validation of user for give url. if this is valid then it will return menuItem.
	 * 
	 * @param user User for which we need to check the rights on specified menu item
	 * @param currentMenu Current menu under which specified menu item exists
	 * @param menuItemURL URL of menu item for which we need to check the rights of specified user
	 * @return Menu Item object, if user has right on it
	 * @throws SecurityException If user does not have right, or any other problem occurs
	 */
	public MenuItem validateRequestForMenuItem( User user, Menu currentMenu, String menuItemURL )
			throws SecurityException
	{
		LOGGER.trace( "validateRequestForMenuItem: user[" + user + "] currentMenu[" + currentMenu + "] menuItemURL["
				+ menuItemURL + "]" );

		if( user == null && currentMenu == null )
		{
			SecurityException se = new SecurityException( IErrorCodes.AUTHENTICATION_FAILURE,
					"User or Current Menu found null. It seems like user is not authorized or have no access on menu item. user["
							+ user + "] currentMenu[" + currentMenu + "] URL[" + menuItemURL + "]" );
			ExceptionUtils.logException( LOGGER, null, se );
			throw se;
		}
		else
		{
			MenuItem menuItem = user.getMenuItemForURL( currentMenu, menuItemURL );
			if( menuItem == null )
			{
				SecurityException se = new SecurityException( IErrorCodes.AUTHENTICATION_FAILURE,
						"No menu item found for current request. It seems like user is not authorized for menu item. user["
								+ user + "] currentMenu[" + currentMenu + "] URL[" + menuItemURL + "]" );
				LOGGER.info( se.getMessage() );
				throw se;
			}
			return menuItem;
		}
	}

	public Collection<ApplicationDomain> getApplicationDomains( User user ) throws SecurityException
	{
		return getSecurityService().getApplicationDomains( user );
	}

	/**
	 * It return the processed request URI, specific to application. Application may use different standards of URI to
	 * check the validations. This is called mainly from Security Filters
	 * 
	 * It process the URI of request and process it to get a URL which have only first parameter with it. So it strips
	 * all trailing parameters and use the first one only for validation purpose.
	 * 
	 * @param httpRequest Request
	 * @return process Request URI
	 */
	public String getProcessedRequestURI( HttpServletRequest httpRequest )
	{
		Utilities.assertNotNullArgument( httpRequest );

		String requestURI = httpRequest.getRequestURI();
		String queryString = httpRequest.getQueryString();

		// Example
		// Request URI: /PBAccountPayable/menuItem.do
		// Request URL: http://localhost:8181/PBAccountPayable/menuItem.do
		// Query String: item-id=3843

		LOGGER.trace( "getProcessedRequestURI: requestURI[" + requestURI + "] queryString[" + queryString + "]" );

		requestURI = requestURI.substring( requestURI.lastIndexOf( "/" ) + 1, requestURI.length() );
		if( !StringUtils.isQualifiedString( queryString ) )
		{
			LOGGER.debug( "Null query string found!" );
			return requestURI;
		}

		if( queryString.contains( "&" ) )
		{
			queryString = queryString.substring( 0, queryString.indexOf( '&' ) );
		}
		requestURI = requestURI + "?" + queryString;
		LOGGER.debug( "updated-requestURI-without-amp[ " + requestURI + " ] queryString[ " + queryString + " ]" );

		return requestURI;
	}

	/**
	 * This method will process the URI for security validations as per application logic. This is generally called from
	 * application components like Button Tag
	 * 
	 * @param completeURI Complete URI
	 * @return Processed URI. It will be the portion of URL after last slash, including first parameter if exists
	 */
	public String getProcessedRequestURI( String completeURI )
	{
		Utilities.assertNotNullArgument( completeURI );

		// Example
		// Request URI: menuItem.do?item-id=6876
		// OR : /PB/menuitem.do?item-id=9877&menu=y

		LOGGER.trace( "getProcessedRequestURI: completeURI[" + completeURI + "]" );

		int indexOfLastSlash = completeURI.lastIndexOf( "/" );
		int indexOfFirstAmp = completeURI.indexOf( "&" );
		String processedURI = completeURI.substring( indexOfLastSlash < 0 ? 0 : indexOfLastSlash + 1,
				indexOfFirstAmp > 0 ? indexOfFirstAmp : completeURI.length() );

		LOGGER.debug( "updated-requestURI-with-firstparam[ " + processedURI + " ]" );

		return processedURI;
	}

	/**
	 * It generates the temporary password and sends that generated password to user mail id.
	 * 
	 * @param userName id of the user
	 * @param mail id of the user
	 * @return boolean true, if password is generated and mail sent to user.
	 * @throws ApplicationException If any problem exists.
	 */
	public boolean startPwdRecoverRequest( String userName, String emailId ) throws ApplicationException
	{
		LOGGER.trace( "startPwdRecoverRequest: userName[" + userName + "] emailId[" + emailId + "]" );
		StringUtils.assertQualifiedArgument( userName );
		StringUtils.assertQualifiedArgument( emailId );
		return getSecurityService().startPwdRecoverRequest( userName, emailId );
	}

	public static void main( String[] args )
	{
		String processedURI = new SecurityManager().getProcessedRequestURI( "/PB/menuitem.do?item-id=9877&menu=y" );
		System.err.println( "processedURI[" + processedURI + "]" );

		processedURI = new SecurityManager().getProcessedRequestURI( "menuItem.do?item-id=6876" );
		System.err.println( "processedURI[" + processedURI + "]" );

		processedURI = new SecurityManager().getProcessedRequestURI( "/menuItem.do?item-id=6876" );
		System.err.println( "processedURI[" + processedURI + "]" );

		processedURI = new SecurityManager().getProcessedRequestURI( "/menuItem.do?item-id=6876&menu=yes" );
		System.err.println( "processedURI[" + processedURI + "]" );

		processedURI = new SecurityManager().getProcessedRequestURI(
				"cashFlowPlan.do?action=managecashplandetail&className=com.etc.publicbooks.common.bdo.ap.budgetadm.CashPlan&id=15" );
		System.err.println( "processedURI[" + processedURI + "]" );

		processedURI = new SecurityManager()
				.getProcessedRequestURI( "http://www.publicbooks.com/menuItem.do?item-id=6876&menu=yes" );
		System.err.println( "processedURI[" + processedURI + "]" );

	}
}