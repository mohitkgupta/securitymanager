package org.vedantatree.securitymanager;

import java.util.Collection;

import org.vedantatree.exceptions.ApplicationException;
import org.vedantatree.securitymanager.model.Application;
import org.vedantatree.securitymanager.model.ApplicationDomain;
import org.vedantatree.securitymanager.model.Menu;
import org.vedantatree.securitymanager.model.MenuItem;
import org.vedantatree.securitymanager.model.User;
import org.vedantatree.securitymanager.model.UserRole;


/**
 * Object of this type represent a service which provides all information related to security objects and related
 * operations.
 * 
 * <p>
 * In current context, Supervision module is one of the security service which can provide us all security related
 * information. However, later, it can be replaced easily with any other plugin like third party tools also.
 * 
 * @author Mohit Gupta <mohit.gupta@vedantatree.com>
 */
public interface ISecurityService
{

	/**
	 * It returns the User for specified name and password
	 * 
	 * @param userName Id of the user
	 * @param password Password for the user
	 * @return User object if Security service has some user corresponding to specified information
	 * @throws SecurityException If user does not exist with specified information
	 * @throws If there is any problem other than above
	 */
	User getUser( String userName, String password, String applicationName ) throws ApplicationException,
			SecurityException;

	/**
	 * This method will return the collection of the menus associated with the specified user for all roles
	 * 
	 * @param user User object for which service need to return the menus
	 * @return Collection of menus for specified user
	 * @throws SecurityException If there is any problem
	 */
	Collection<Menu> getMenus( User user ) throws SecurityException;

	/**
	 * It returns the Set of MenuItems for the specified menu
	 * 
	 * @param menu Menu for which service need to return the menu items
	 * @return Collection of menu items for specified menu
	 * @throws SecurityException If there is any problem
	 */
	Collection<MenuItem> getMenuItems( Menu menu ) throws SecurityException;

	/**
	 * This method returns the collection of all applications registered with Security Service.
	 * 
	 * @return Collection of all applications
	 * @throws SecurityException If there is any problem
	 */
	Collection<Application> getAllApplications() throws SecurityException;

	/**
	 * This method returns the collection of all applications in which specified user is registered
	 * 
	 * @param user User object for which service needs to return the applications
	 * @return Collection of applications for specified user
	 * @throws SecurityException If there is any problem
	 */
	Collection<Application> getApplications( User user );

	/**
	 * It returns the list of ObjectGroups associated with any user.
	 * 
	 * @param user User object for which we need to return the application domains
	 * @return Collection of application domains
	 * @throws SecurityException If there is any problem
	 */
	Collection<ApplicationDomain> getApplicationDomains( User user ) throws SecurityException;

	/**
	 * It returns the roles associated with user
	 * 
	 * @param user User object for which service needs to return the collection of roles
	 * @throws SecurityException If there is any problem
	 */

	Collection<UserRole> getUserRoles( User user ) throws SecurityException;

	/**
	 * It returns all roles for the application
	 * 
	 * @return Collection of all roles available with security service
	 * @throws SecurityException If there is any problem
	 */
	Collection<UserRole> getApplicationRoles() throws SecurityException;
	
	/**
	 * It generates the temporary password  and sends that generated password to user mail id.
	 * 
	 * @param userName id of the user
	 * @param mail id of the user
	 * @return boolean true, if password is generated and mail sent to user.
	 * @throws SecurityException If any problem exists.
	 */
	public boolean startPwdRecoverRequest( String userName, String emailId ) throws ApplicationException;
}
