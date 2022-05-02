/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.jwt;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.lsoftware.inventory.authentication.authorities.AuthoritiesCustomProvider;

import io.jsonwebtoken.security.Keys;

/**
 * The Class JWTAuthorizationFilterTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {

	/** The Constant SECRET_KEY. */
	private static final String SECRET_KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure";

	/** The Constant TOKEN_EXAMPLE. */
	private static final String TOKEN_EXAMPLE = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMVUlTMyIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImlhdCI6MTY1MTQ1MzgwMSwiZXhwIjoxOTY2NzQxMjAwfQ.BGqEfoxOHKz4pYpVyLm5RqWRkPlXcW3AWTSbzgdm5uzQI6t2HIe-qXXObWR-aaWhOy9C0plKMXfCJxYYNwuInA";
	
	/** The Constant TOKEN_INVALID_EXAMPLE. */
	private static final String TOKEN_INVALID_EXAMPLE = "Bearer eayJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWlzMyIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImlhdCI6MTY1MDIyMzk3NiwiZXhwIjoxNjUxMDM1NjAwfQ.wGYgnNdA6r_8k0EYmjE0Cs_e3FSygPfaRknlmLaS4NdPBK7NeM1LC_lx0_N8A706SnoAe9uEZ-2uSIZN28klYA";
	
	/** The under test. */
	private JWTAuthorizationFilter underTest;

	/** The jwt config. */
	@Mock
	private JWTConfig jwtConfig;
	
	/** The http servlet request. */
	@Mock
	private HttpServletRequest httpServletRequest;
	
	/** The http servlet response. */
	@Mock
	private HttpServletResponse httpServletResponse;
	
	/** The filter chain. */
	@Mock
	private FilterChain filterChain;

	/** The authorities custom provider. */
	@Mock
	private AuthoritiesCustomProvider authoritiesCustomProvider;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new JWTAuthorizationFilter(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), jwtConfig,
				authoritiesCustomProvider);
	}

	/**
	 * It should fail for null header.
	 *
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	void itShouldFailForNullHeader() throws ServletException, IOException {
		underTest.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
		verify(filterChain, times(1)).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}
	
	/**
	 * It should do filter internal valid token.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Test
	void itShouldDoFilterInternalValidToken() throws IOException, ServletException {
		
		BDDMockito.given(httpServletRequest.getHeader(any()))
			.willReturn(TOKEN_EXAMPLE);
		
		BDDMockito.given(jwtConfig.getTokenPrefix())
			.willReturn("Bearer");
		
		underTest.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
		verify(authoritiesCustomProvider, times(1)).provideGrantedAuthorities(any());
		verify(filterChain, times(1)).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}
	
	/**
	 * It should do filter internal in valid token.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Test
	void itShouldDoFilterInternalInValidToken() throws IOException, ServletException {
		
		BDDMockito.given(httpServletRequest.getHeader(any()))
			.willReturn(TOKEN_INVALID_EXAMPLE);
		
		BDDMockito.given(jwtConfig.getTokenPrefix())
			.willReturn("Bearer");
		
		
		assertThatThrownBy(() -> underTest.doFilterInternal(httpServletRequest, httpServletResponse, filterChain))
			.isInstanceOf(AccessDeniedException.class)
			.hasMessageContaining(String.format("Invalid Token %s", 
					TOKEN_INVALID_EXAMPLE.replace("Bearer", "")));
		
		verify(authoritiesCustomProvider, times(0)).provideGrantedAuthorities(any());
		verify(filterChain, times(0)).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}

}
