/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The Class UserAuthentication.
 * 
 * @author Luis Espinosa
 */
public class UserAuthentication implements UserDetails {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 443807688123839270L;

	/** The username. */
	private final String username;
	
	/** The password. */
	private final String password;
	
	/** The granted authorities. */
	private final Set<? extends GrantedAuthority> grantedAuthorities;
	
	/** The is account non expired. */
	private final boolean isAccountNonExpired;
	
	/** The is account non locked. */
	private final boolean isAccountNonLocked;
	
	/** The is credentials non expired. */
	private final boolean isCredentialsNonExpired;
	
	/** The is enabled. */
	private final boolean isEnabled;

	/**
	 * Instantiates a new app authentication user.
	 *
	 * @param username the username
	 * @param password the password
	 * @param grantedAuthorities the granted authorities
	 * @param isAccountNonExpired the is account non expired
	 * @param isAccountNonLocked the is account non locked
	 * @param isCredentialsNonExpired the is credentials non expired
	 * @param isEnabled the is enabled
	 */
	public UserAuthentication(String username, String password, Set<? extends GrantedAuthority> grantedAuthorities,
			boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired,
			boolean isEnabled) {
		this.username = username;
		this.password = password;
		this.grantedAuthorities = grantedAuthorities;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
		this.isEnabled = isEnabled;
	}

	/**
	 * Gets the authorities.
	 *
	 * @return the authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Checks if is account non expired.
	 *
	 * @return true, if is account non expired
	 */
	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	/**
	 * Checks if is account non locked.
	 *
	 * @return true, if is account non locked
	 */
	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	/**
	 * Checks if is credentials non expired.
	 *
	 * @return true, if is credentials non expired
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
