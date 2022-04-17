/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The Class UserDetailsCustomServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsCustomServiceTest {
	
	/** The under test. */
	private UserDetailsCustomService underTest;
	
	/** The user application provider dao. */
	@Mock
	private UserApplicationProviderDao userApplicationProviderDao;
	
	/**
	 * Setup.
	 */
	@BeforeEach
	void setup() {
		underTest = new UserDetailsCustomService(userApplicationProviderDao);
	}

	/**
	 * It should throw exception when loading user by username.
	 */
	@Test
	void itShouldThrowExceptionWhenLoadingUserByUsername() {
		String username = "luis3";
		
		assertThatThrownBy(() -> underTest.loadUserByUsername(username))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessageContaining(String.format("Username %s not found", username));
		
		//verify(userApplicationProviderDao, never()).save(any()); // DONT DELETE
	}
	
	/**
	 * It shouldn load user by username.
	 */
	@Test
	void itShouldnLoadUserByUsername() {
		String username = "luis3";
		
		Optional<UserAuthentication> userOptional = getUserAuthentication();
		
		BDDMockito.given(userApplicationProviderDao.
				provideApplicationUserByUsername(anyString()))
				.willReturn(userOptional);
		
		UserDetails userDetails = underTest.loadUserByUsername(username);
		
		ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
		verify(userApplicationProviderDao).
			provideApplicationUserByUsername(usernameCaptor.capture());
		
		String captured = usernameCaptor.getValue();
		assertThat(captured).isEqualTo(username);
		
		assertThat(userDetails.getUsername()).isEqualTo(userOptional.get().getUsername());
	}
	
	
	/**
	 * Gets the user authentication.
	 *
	 * @return the user authentication
	 */
	private Optional<UserAuthentication> getUserAuthentication() {
		
		return Optional.of(new UserAuthentication("luis3", 
				"123456", Set.of(new SimpleGrantedAuthority("ADMIN")),
				true, true, true, true));
	}

}
