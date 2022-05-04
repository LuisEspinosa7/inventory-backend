/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.authentication.request.RequestAuthenticationData;
import com.lsoftware.inventory.mappings.MappingsCustom;
import com.lsoftware.inventory.shared.api.ApiCustomResponse;

import io.jsonwebtoken.Jwts;

/**
 * The Class JwtUsernameAndPasswordAuthenticationFilter.
 * 
 * @author Luis Espinosa
 */
public class JWTUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	/** The authentication manager. */
	private final AuthenticationManager authenticationManager;
    
    /** The jwt config. */
    private final JWTConfig jwtConfig;
    
    /** The secret key. */
    private final SecretKey secretKey;
    
    /** The object mapper. */
    private final MappingsCustom mappingsCustom;

    /** The object mapper. */
    private final ObjectMapper objectMapper;
    
    /**
     * Instantiates a new jwt username and password authentication filter.
     *
     * @param authenticationManager the authentication manager
     * @param jwtConfig the jwt config
     * @param secretKey the secret key
     * @param MappingsCustom the object mapper
     */
    public JWTUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JWTConfig jwtConfig,
                                                      SecretKey secretKey,
                                                      MappingsCustom mappingsCustom,
                                                      ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.mappingsCustom = mappingsCustom;
        this.objectMapper = objectMapper;
    }

    /**
     * Attempt authentication.
     *
     * @param request the request
     * @param response the response
     * @return the authentication
     * @throws AuthenticationException the authentication exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            RequestAuthenticationData authenticationRequest = mappingsCustom
                    .readValue(request.getInputStream(), RequestAuthenticationData.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            return authenticationManager.authenticate(authentication); // returns Authentication

        } catch (Exception e) {
            throw new AccessDeniedException(e.getMessage());
        }

    }

    /**
     * Successful authentication.
     *
     * @param request the request
     * @param response the response
     * @param chain the chain
     * @param authResult the auth result
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    	
    	Set<String> authorities = authResult.getAuthorities()
    			.stream()
    			.map(g -> g.getAuthority().trim().replace("authority=", ""))
    			.collect(Collectors.toSet());
    	
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix().concat(" ") + token);
    }
    
    
    /**
     * Unsuccessful authentication.
     *
     * @param request the request
     * @param response the response
     * @param failed the failed
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		SecurityContextHolder.clearContext();
		response.setContentType("application/json; charset=UTF-8");
		
		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(HttpStatus.FORBIDDEN.toString().split(" ")[0]))
				.message(failed.getMessage())
				.path("")
				.build();
		
		response.setStatus(403);
		response.getWriter().print(objectMapper.writeValueAsString(error));
		response.getWriter().flush();
	}
	

}
