/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.jwt;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

/**
 * The Class JWTSecretKeyConfig.
 * 
 * @author Luis Espinosa
 */
@Configuration
public class JWTSecretKeyConfig { 
	
	/** The jwt config. */
	private final JWTConfig jwtConfig;

    /**
     * Instantiates a new JWT secret key.
     *
     * @param jwtConfig the jwt config
     */
    public JWTSecretKeyConfig(JWTConfig jwtConfig) {
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
