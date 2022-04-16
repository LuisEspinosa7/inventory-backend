/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.authentication.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.lsoftware.inventory.authentication.GrantedAuthorityProvider;

import io.jsonwebtoken.Claims;

/**
 * The Class ClaimsGrantedAuthorityImpl.
 * 
 * @author Luis Espinosa
 */
@Component
public class ClaimsGrantedAuthorityImpl implements GrantedAuthorityProvider {
	
	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(ClaimsGrantedAuthorityImpl.class);

	/**
	 * Provide granted authorities.
	 *
	 * @param bodyClaims the body claims
	 * @return the sets the
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<GrantedAuthority> provideGrantedAuthorities(Claims bodyClaims) {
		LOG.info("method: provideGrantedAuthorities");
		List<String> plainAuthorities = (ArrayList<String>) bodyClaims.get("authorities");
		
		Set<GrantedAuthority> authorities = plainAuthorities.stream()
				.map(a -> new SimpleGrantedAuthority("ROLE_" + a))
				.collect(Collectors.toSet());
		
		return authorities;
	}
	
	

}
