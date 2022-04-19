/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.authentication.authorities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


/**
 * The Class AuthoritiesClaimsProviderTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class AuthoritiesClaimsProviderTest {
	
	/** The Constant SECRET_KEY. */
	private static final String SECRET_KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure";
	
	/** The Constant TOKEN_EXAMPLE. */
	private static final String TOKEN_EXAMPLE = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWlzMyIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImlhdCI6MTY1MDIyMzk3NiwiZXhwIjoxNjUxMDM1NjAwfQ.wGYgnNdA6r_8k0EYmjE0Cs_e3FSygPfaRknlmLaS4NdPBK7NeM1LC_lx0_N8A706SnoAe9uEZ-2uSIZN28klYA";
	
	/** The under test. */
	AuthoritiesClaimsProvider underTest;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new AuthoritiesClaimsProvider();
	}
	

	/**
	 * It should provide granted authorities.
	 */
	@Test
	void itShouldProvideGrantedAuthorities() {
		
		Jws<Claims> claimsJws = Jwts.parser()
				.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.parseClaimsJws(TOKEN_EXAMPLE.replace("Bearer", ""));
		Claims body = claimsJws.getBody();
		
		Set<GrantedAuthority> authorities = underTest.provideGrantedAuthorities(body);
		assertThat(authorities.size()).isEqualTo(1);
	}

}
