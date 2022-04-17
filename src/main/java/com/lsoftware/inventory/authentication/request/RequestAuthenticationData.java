/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.authentication.request;

/**
 * The Class UsernameAndPasswordAuthenticationRequest.
 * 
 * @author Luis Espinosa
 */
public class RequestAuthenticationData {

	/** The username. */
	private String username;
	
	/** The password. */
	private String password;

	/**
	 * Instantiates a new username and password authentication request.
	 */
	public RequestAuthenticationData() {
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
