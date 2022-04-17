/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.Optional;

/**
 * The Interface ApplicationUserDao.
 * 
 * @author Luis Espinosa
 */
public interface UserApplicationProviderDao {
	
	/**
	 * Provide application user by username.
	 *
	 * @param username the username
	 * @return the optional
	 */
	Optional<UserAuthentication> provideApplicationUserByUsername(String username);
}
