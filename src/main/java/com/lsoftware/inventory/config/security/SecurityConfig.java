/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.config.security;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.authentication.GrantedAuthorityProvider;
import com.lsoftware.inventory.authentication.impl.AppUserDetailsService;
import com.lsoftware.inventory.config.jwt.JWTConfig;
import com.lsoftware.inventory.filters.jwt.JwtAuthorizationFilter;
import com.lsoftware.inventory.filters.jwt.JwtUsernameAndPasswordAuthenticationFilter;

/**
 * The Class SecurityConfig.
 * 
 * @author Luis Espinosa
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/** The password encoder. */
	private final PasswordEncoder passwordEncoder;
    
    /** The app user details service. */
    private final AppUserDetailsService appUserDetailsService;
    
    /** The secret key. */
    private final SecretKey secretKey;
    
    /** The jwt config. */
    private final JWTConfig jwtConfig;
    
    /** The object mapper. */
    private final ObjectMapper objectMapper;
    
    /** The granted authority provider. */
    GrantedAuthorityProvider grantedAuthorityProvider;

    /**
     * Instantiates a new security config.
     *
     * @param passwordEncoder the password encoder
     * @param appUserDetailsService the app user details service
     * @param secretKey the secret key
     * @param jwtConfig the jwt config
     * @param objectMapper the object mapper
     * @param grantedAuthorityProvider the granted authority provider
     */
    public SecurityConfig(PasswordEncoder passwordEncoder,
    						AppUserDetailsService appUserDetailsService,
                            SecretKey secretKey,
                            JWTConfig jwtConfig,
                            ObjectMapper objectMapper,
                            GrantedAuthorityProvider grantedAuthorityProvider) {
        this.passwordEncoder = passwordEncoder;
        this.appUserDetailsService = appUserDetailsService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.objectMapper = objectMapper;
        this.grantedAuthorityProvider = grantedAuthorityProvider;
    }

    /**
     * Configure.
     *
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	
    	http.cors()
    	.and()
    	.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/api/usuario/signUp/**").permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey, objectMapper))
		.addFilterAfter(new JwtAuthorizationFilter(secretKey, jwtConfig, grantedAuthorityProvider), JwtUsernameAndPasswordAuthenticationFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Configure.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * Dao authentication provider.
     *
     * @return the dao authentication provider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(appUserDetailsService);
        return provider;
    }

}
