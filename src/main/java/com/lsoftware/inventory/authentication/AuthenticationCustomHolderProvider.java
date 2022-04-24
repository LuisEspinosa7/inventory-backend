/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The Class AuthenticationCustomHolderProvider.
 * 
 * @author Luis Espinosa
 */
@Component
public class AuthenticationCustomHolderProvider implements AuthenticationHolderProvider {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationCustomHolderProvider.class);
	
	/**
	 * Provide context holder.
	 *
	 * @return the authentication
	 */
	@Override
	public Authentication provideContextHolder() {
		LOG.info("method: provideContextHolder");
		return SecurityContextHolder.getContext().getAuthentication();
	}

	
}
