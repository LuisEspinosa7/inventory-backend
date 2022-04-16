/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.dao.auth;

import java.util.Optional;

import com.lsoftware.inventory.model.AppAuthenticationUser;

/**
 * The Interface ApplicationUserDao.
 * 
 * @author Luis Espinosa
 */
public interface ApplicationUserDao {
	
	/**
	 * Provide application user by username.
	 *
	 * @param username the username
	 * @return the optional
	 */
	Optional<AppAuthenticationUser> provideApplicationUserByUsername(String username);
}
