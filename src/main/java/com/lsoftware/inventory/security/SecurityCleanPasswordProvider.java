/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lsoftware.inventory.user.User;

/**
 * The Class SecurityCleanPasswordProvider.
 * 
 * @author Luis Espinosa
 */
public class SecurityCleanPasswordProvider implements SecurityCleanPassword<User> {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(SecurityCleanPasswordProvider.class);
	
	/**
	 * Clean sensitive data.
	 *
	 * @param obj the obj
	 */
	@Override
	public void cleanSensitiveData(User obj) {
		LOG.info("method: cleanSensitiveData");
		
		obj.setPassword("");
	}

}
