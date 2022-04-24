/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.authentication;

import org.springframework.security.core.Authentication;

/**
 * The Interface AuthenticationHolderProvider.
 * 
 * @author Luis Espinosa
 */
public interface AuthenticationHolderProvider {
	
	/**
	 * Provide context holder.
	 *
	 * @return the authentication
	 */
	Authentication provideContextHolder();

}
