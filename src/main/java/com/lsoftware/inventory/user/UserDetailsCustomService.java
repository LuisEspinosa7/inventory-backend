/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The Class AppUserDetailsService.
 * 
 * @author Luis Espinosa
 */
@Service
public class UserDetailsCustomService implements UserDetailsService {

	/** The application user dao. */
	private final UserApplicationProviderDao applicationUserDao;

	/**
	 * Instantiates a new app user details service.
	 *
	 * @param applicationUserDao the application user dao
	 */
	public UserDetailsCustomService(@Qualifier("authPostgres") UserApplicationProviderDao applicationUserDao) {
		this.applicationUserDao = applicationUserDao;
	}

	/**
	 * Load user by username.
	 *
	 * @param username the username
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return applicationUserDao.provideApplicationUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	}

}
