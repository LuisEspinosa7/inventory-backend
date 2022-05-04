/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.jwt;

import java.io.IOException;


import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;
import com.lsoftware.inventory.authentication.authorities.AuthoritiesCustomProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * The Class JwtAuthorizationFilter.
 * 
 * @author Luis Espinosa
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	/** The log. */
	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	
	/** The secret key. */
	private final SecretKey secretKey;
	
	/** The jwt config. */
	private final JWTConfig jwtConfig;
	
	/** The granted authority provider. */
	private final AuthoritiesCustomProvider grantedAuthorityProvider;

	/**
	 * Instantiates a new jwt authorization filter.
	 *
	 * @param secretKey the secret key
	 * @param jwtConfig the jwt config
	 * @param grantedAuthorityProvider the granted authority provider
	 */
	public JWTAuthorizationFilter(SecretKey secretKey,
							JWTConfig jwtConfig,
							AuthoritiesCustomProvider grantedAuthorityProvider) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.grantedAuthorityProvider = grantedAuthorityProvider;
    }

	/**
	 * Do filter internal.
	 *
	 * @param request the request
	 * @param response the response
	 * @param filterChain the filter chain
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOG.info("method: doFilterInternal");
		
		String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

		if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

		try {

			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

			Claims body = claimsJws.getBody();

			String username = body.getSubject();
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
					grantedAuthorityProvider.provideGrantedAuthorities(body));

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (JwtException e) {
			throw new AccessDeniedException(String.format("Invalid Token %s", token));
		}

		filterChain.doFilter(request, response);
	}
}
