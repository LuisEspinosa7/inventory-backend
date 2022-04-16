/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.authentication;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;

/**
 * The Interface IGrantedAuthorityProvider.
 * 
 * @author Luis Espinosa
 */
public interface GrantedAuthorityProvider {
	
	/**
	 * Provide granted authorities.
	 *
	 * @param bodyClaims the body claims
	 * @return the sets the
	 */
	Set<GrantedAuthority> provideGrantedAuthorities(Claims bodyClaims);

}
