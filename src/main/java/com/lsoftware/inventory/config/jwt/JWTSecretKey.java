/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.config.jwt;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

/**
 * The Class JWTSecretKey.
 * 
 * @author Luis Espinosa
 */
@Configuration
public class JWTSecretKey { 
	
	/** The jwt config. */
	private final JWTConfig jwtConfig;

    /**
     * Instantiates a new JWT secret key.
     *
     * @param jwtConfig the jwt config
     */
    public JWTSecretKey(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Secret key.
     *
     * @return the secret key
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

}
