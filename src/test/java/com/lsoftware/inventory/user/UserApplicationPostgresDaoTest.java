/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lsoftware.inventory.role.Role;

/**
 * The Class UserApplicationPostgresDaoTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class UserApplicationPostgresDaoTest {
	
	/** The under test. */
	private UserApplicationPostgresDao underTest;
	
	/** The user repository. */
	@Mock
	private UserRepository userRepository;
	
	
	/**
	 * Setup.
	 */
	@BeforeEach
	void setup() {
		underTest = new UserApplicationPostgresDao(userRepository);
	}
	

	/**
	 * It should not provide user by username.
	 */
	@Test
	void itShouldNotProvideUserByUsername() {
		String username = "luis3";
		Optional<UserAuthentication> response = 
				underTest.provideApplicationUserByUsername(username); //when
		
		// then
		verify(userRepository).findByUsername(Mockito.anyString());
		assertThat(response.isEmpty()).isTrue();
	}
	
	/**
	 * It should provide roles with user by username.
	 */
	@Test
	void itShouldProvideRolesWithUserByUsername() {
		String username = "luis3";
		
		BDDMockito.given(userRepository.findByUsername(anyString()))
			.willReturn(getUserAuthentication(true));
		
		Optional<UserAuthentication> response = 
				underTest.provideApplicationUserByUsername(username);
	
		ArgumentCaptor<String> usernameArgumentCaptor = 
				ArgumentCaptor.forClass(String.class);
		
		verify(userRepository).findByUsername(usernameArgumentCaptor.capture());
		String capturedUsername = usernameArgumentCaptor.getValue();
		assertThat(capturedUsername).isEqualTo(username);
		
		assertThat(response.get().getUsername()).isEqualTo(username);
	}
	
	
	/**
	 * It should provide empty roles user by username.
	 */
	@Test
	void itShouldProvideEmptyRolesUserByUsername() {
		String username = "luis3";
		
		Optional<User> dbExpected = getUserAuthentication(false);
		BDDMockito.given(userRepository.findByUsername(anyString()))
			.willReturn(dbExpected);
		
		Optional<UserAuthentication> response = 
				underTest.provideApplicationUserByUsername(username);
		
		ArgumentCaptor<String> usernameArgumentCaptor = 
				ArgumentCaptor.forClass(String.class);
		
		verify(userRepository).findByUsername(usernameArgumentCaptor.capture());
		String capturedUsername = usernameArgumentCaptor.getValue();
		assertThat(capturedUsername).isEqualTo(username);
		
		assertThat(response.get().getUsername()).isEqualTo(username);
		assertThat(response.get().getPassword()).isEqualTo(dbExpected.get().getPassword());
		assertThat(response.get().getAuthorities().size()).isEqualTo(dbExpected.get().getRoles().size());
		assertThat(response.get().isAccountNonExpired()).isTrue();
		assertThat(response.get().isAccountNonLocked()).isTrue();
		assertThat(response.get().isCredentialsNonExpired()).isTrue();
		assertThat(response.get().isEnabled()).isTrue();
	}
	
	
	/**
	 * Gets the user authentication.
	 *
	 * @param hasRoles the has roles
	 * @return the user authentication
	 */
	private Optional<User> getUserAuthentication(boolean hasRoles) {
		
		Set<Role> roles = Set.of(new Role(1L, "ADMIN", "DESCRIPTION"), 
				new Role(2L, "SUPERVISOR", "DESCRIPTION"));
		
		User user = new User();
		user.setId(1L);
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		if (hasRoles) user.setRoles(roles);
		
		return Optional.of(user);
	}

}
