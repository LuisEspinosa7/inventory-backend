/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.authentication.request.RequestAuthenticationData;
import com.lsoftware.inventory.mappings.MappingsCustom;

import io.jsonwebtoken.security.Keys;

/**
 * The Class JWTUsernameAndPasswordAuthenticationFilterTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class JWTUsernameAndPasswordAuthenticationFilterTest {
	
	/** The Constant SECRET_KEY. */
	private static final String SECRET_KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure";
	
	/** The under test. */
	private JWTUsernameAndPasswordAuthenticationFilter underTest;
	
	/** The authentication manager. */
	@Mock
	private AuthenticationManager authenticationManager;
	
	/** The jwt config. */
	@Mock
	private JWTConfig jwtConfig;
	
	/** The object mapper. */
	@Mock
	private ObjectMapper objectMapper; 
	
	/** The mappings custom. */
	@Mock
	private MappingsCustom mappingsCustom;
	
	/** The http servlet request. */
	@Mock
	private HttpServletRequest httpServletRequest;
	
	/** The http servlet response. */
	@Mock
	private HttpServletResponse httpServletResponse;
	
	/** The authentication. */
	@Mock
	private Authentication authentication;
	
	/** The filter chain. */
	@Mock
	private FilterChain filterChain;
	
	/** The authentication exception. */
	@Mock
	private AuthenticationException authenticationException;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new JWTUsernameAndPasswordAuthenticationFilter(authenticationManager, jwtConfig, 
				Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), mappingsCustom, objectMapper);
	}

	/**
	 * It should throws exception when attempting authentication.
	 *
	 * @throws JsonMappingException the json mapping exception
	 * @throws JsonProcessingException the json processing exception
	 */
	@Test
	void itShouldThrowsExceptionWhenAttemptingAuthentication() throws JsonMappingException, JsonProcessingException {
		
		assertThatThrownBy(() -> 
			underTest.attemptAuthentication(httpServletRequest, httpServletResponse))
			.isInstanceOf(RuntimeException.class);
		
		verify(authenticationManager, never()).authenticate(any());
	}
	
	/**
	 * It should attempt authentication successfull.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	void itShouldAttemptAuthenticationSuccessfull() throws IOException {
		
		RequestAuthenticationData request = new RequestAuthenticationData();
		request.setUsername("luis3");
		request.setPassword("123456");
		
		when(mappingsCustom.readValue(any(), any())).thenReturn(request);
		
		BDDMockito.given(authenticationManager.authenticate(any())).willReturn(new TemporalAuthorization());
		
		Authentication response = underTest.attemptAuthentication(httpServletRequest, httpServletResponse);
		
		verify(authenticationManager, times(1)).authenticate(any());
		assertThat(response.getPrincipal()).isEqualTo("luis3");
	}
	
	
	/**
	 * It should configure token no authorities.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Test
	void itShouldConfigureTokenNoAuthorities() throws IOException, ServletException {
		
		BDDMockito.given(authentication.getName()).willReturn("luis3");
		BDDMockito.given(jwtConfig.getTokenExpirationAfterDays()).willReturn(3);
		BDDMockito.given(jwtConfig.getAuthorizationHeader()).willReturn("Authorization");
		BDDMockito.given(jwtConfig.getTokenPrefix()).willReturn("Bearer ");
		
		underTest.successfulAuthentication(httpServletRequest, httpServletResponse, filterChain, authentication);
		
		verify(httpServletResponse, times(1)).addHeader(anyString(), anyString());
	}
	
	
	/**
	 * It should configure token with authorities.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Test
	void itShouldConfigureTokenWithAuthorities() throws IOException, ServletException {
		
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"));
		
		doReturn(authorities).when(authentication).getAuthorities();
		
		BDDMockito.given(authentication.getName()).willReturn("luis3");
		BDDMockito.given(jwtConfig.getTokenExpirationAfterDays()).willReturn(3);
		BDDMockito.given(jwtConfig.getAuthorizationHeader()).willReturn("Authorization");
		BDDMockito.given(jwtConfig.getTokenPrefix()).willReturn("Bearer ");
		
		underTest.successfulAuthentication(httpServletRequest, httpServletResponse, filterChain, authentication);
		
		verify(httpServletResponse, times(1)).addHeader(anyString(), anyString());
	
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
		verify(httpServletResponse).addHeader(anyString(), argumentCaptor.capture());
		
		String captured = argumentCaptor.getValue();
		assertThat(captured).isNotEmpty();
	}
	
	/**
	 * It should not authenticate successfully.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Test
	void itShouldNotAuthenticateSuccessfully() throws IOException, ServletException {
		
		BDDMockito.given(authenticationException.getMessage()).willReturn("Forbidden");
		BDDMockito.given(httpServletResponse.getWriter()).willReturn(new PrintWriter("file.txt"));
		
		underTest.unsuccessfulAuthentication(httpServletRequest, httpServletResponse, authenticationException);
	
		verify(httpServletResponse, times(1)).setStatus(anyInt());
		verify(httpServletResponse, times(2)).getWriter();
	}
	
	
	
	/**
	 * The Class TemporalAuthorization.
	 */
	private static class TemporalAuthorization implements Authentication {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		@Override
		public String getName() {
			return "luis3";
		}

		/**
		 * Gets the authorities.
		 *
		 * @return the authorities
		 */
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		/**
		 * Gets the credentials.
		 *
		 * @return the credentials
		 */
		@Override
		public Object getCredentials() {
			return "123456";
		}

		/**
		 * Gets the details.
		 *
		 * @return the details
		 */
		@Override
		public Object getDetails() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * Gets the principal.
		 *
		 * @return the principal
		 */
		@Override
		public Object getPrincipal() {
			return "luis3";
		}

		/**
		 * Checks if is authenticated.
		 *
		 * @return true, if is authenticated
		 */
		@Override
		public boolean isAuthenticated() {
			return true;
		}

		/**
		 * Sets the authenticated.
		 *
		 * @param isAuthenticated the new authenticated
		 * @throws IllegalArgumentException the illegal argument exception
		 */
		@Override
		public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	

}
