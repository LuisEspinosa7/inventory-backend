/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * The Class UserRepositoryTest.
 * 
 * @author Luis Espinosa
 */
@DataJpaTest
class UserRepositoryTest {
	
	/** The under test. */
	@Autowired
	private UserRepository underTest;
	
	
	/**
	 * Tear down.
	 */
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	

	/**
	 * It should find user by username.
	 */
	@Test
	void itShouldFindUserByUsername() {
		
		String username = "luis3";
		User user = new User();
		user.setId(1L);
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		
		underTest.save(user);
		
		Optional<User> expected = underTest.findByUsername(username);
		assertThat(expected.isPresent()).isTrue();
		assertThat(expected.get().getId()).isEqualTo(user.getId());
		assertThat(expected.get().getDocument()).isEqualTo(user.getDocument());
		assertThat(expected.get().getName()).isEqualTo(user.getName());
		assertThat(expected.get().getLastName()).isEqualTo(user.getLastName());
		assertThat(expected.get().getUsername()).isEqualTo(user.getUsername());
		assertThat(expected.get().getPassword()).isEqualTo(user.getPassword());
	}
	
	
	/**
	 * It should not find user by username.
	 */
	@Test
	void itShouldNotFindUserByUsername() {
		
		String username = "luis3";
		
		Optional<User> expected = underTest.findByUsername(username);
		assertThat(expected.isPresent()).isFalse();
	}

}
