/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * The Class JWTConfig.
 * 
 * @author Luis Espinosa
 */
@ConfigurationProperties(prefix = "application.jwt")
@Component
public class JWTConfig {
	
	/** The secret key. */
	private String secretKey;
    
    /** The token prefix. */
    private String tokenPrefix;
    
    /** The token expiration after days. */
    private Integer tokenExpirationAfterDays;

    /**
     * Instantiates a new JWT config.
     */
    public JWTConfig() {
    }

    /**
     * Gets the secret key.
     *
     * @return the secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the secret key.
     *
     * @param secretKey the new secret key
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Gets the token prefix.
     *
     * @return the token prefix
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * Sets the token prefix.
     *
     * @param tokenPrefix the new token prefix
     */
    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * Gets the token expiration after days.
     *
     * @return the token expiration after days
     */
    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    /**
     * Sets the token expiration after days.
     *
     * @param tokenExpirationAfterDays the new token expiration after days
     */
    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    /**
     * Gets the authorization header.
     *
     * @return the authorization header
     */
    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

}
