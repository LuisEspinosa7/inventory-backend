/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.lsoftware.inventory.role.Role;
import com.lsoftware.inventory.shared.status.Status;

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
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
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
	
	
	
	
	/**
	 * It should find by document and username and status.
	 */
	@Test
	void itShouldFindByDocumentAndUsernameAndStatus() {
		
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		
		Optional<User> expected = underTest.findByDocumentAndUsernameAndStatus(user.getDocument(), user.getUsername(), 
				List.of(Status.ACTIVE.getDigit()));
		assertThat(expected.isPresent()).isTrue();
		assertThat(expected.get().getDocument()).isEqualTo(user.getDocument());
		assertThat(expected.get().getUsername()).isEqualTo(user.getUsername());
		assertThat(expected.get().getStatus()).isEqualTo(user.getStatus());
	}
	
	
	
	/**
	 * It should find by id and status.
	 */
	@Test
	void itShouldFindByIdAndStatus() {
		
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		
		Optional<User> expected = underTest.findByIdAndStatus(user.getId(), 
				List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()));
		assertThat(expected.isPresent()).isTrue();
		assertThat(expected.get().getDocument()).isEqualTo(user.getDocument());
		assertThat(expected.get().getUsername()).isEqualTo(user.getUsername());
		assertThat(expected.get().getStatus()).isEqualTo(user.getStatus());
	}
	
	
	/**
	 * It should find by id and status then set status by id.
	 */
	@Test
	void itShouldFindByIdAndStatusThenSetStatusById() {
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		
		Optional<User> expected = underTest.findByIdAndStatus(user.getId(), 
				List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()));
		assertThat(expected.isPresent()).isTrue();
		
		int modified = underTest.setStatusById(Status.INACTIVE.getDigit(), expected.get().getId());
		assertThat(modified).isEqualTo(1);
	}
	
	
	/**
	 * It should find by status.
	 */
	@Test
	void itShouldFindByStatus() {
		
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		underTest.save(new User(2L, "987654321", "Valentina", "Espinosa", "valen4", "123456", Status.DELETED.getDigit(), 
				Set.of(new Role(1L, "ADMIN", "DESCRIPTION"))));
		
		Page<User> expected = underTest.findByStatus(List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()), 
				PageRequest.of(0, 2));
		assertThat(expected.getContent().size()).isEqualTo(1);
	}
	
	
	
	/**
	 * It should find by terms containing.
	 */
	@Test
	void itShouldFindByTermsContaining() {
		
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		underTest.save(new User(2L, "987654321", "Valentina", "Espinosa", "valen4", "123456", Status.DELETED.getDigit(), 
				Set.of(new Role(1L, "ADMIN", "DESCRIPTION"))));
		underTest.save(new User(3L, "434343434", "Fabio", "Espinosa", "fabio5", "123456", Status.ACTIVE.getDigit(), 
				Set.of(new Role(1L, "ADMIN", "DESCRIPTION"))));
		
		Page<User> expected = underTest.findByTermsContaining("Fa", List.of(Status.ACTIVE.getDigit()), 
				PageRequest.of(0, 2));
		assertThat(expected.getContent().size()).isEqualTo(1);
	}
	
	
	/**
	 * It should find user by username and status.
	 */
	@Test
	void itShouldFindUserByUsernameAndStatus() {
		
		String username = "luis3";
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		
		Optional<User> expected = underTest.findByUsernameAndStatus(username, Status.ACTIVE.getDigit());
		assertThat(expected.isPresent()).isTrue();
		assertThat(expected.get().getId()).isEqualTo(user.getId());
		assertThat(expected.get().getDocument()).isEqualTo(user.getDocument());
		assertThat(expected.get().getName()).isEqualTo(user.getName());
		assertThat(expected.get().getLastName()).isEqualTo(user.getLastName());
		assertThat(expected.get().getUsername()).isEqualTo(user.getUsername());
		assertThat(expected.get().getPassword()).isEqualTo(user.getPassword());
	}

	
	/**
	 * It should find by id and status then set password by id.
	 */
	@Test
	void itShouldFindByIdAndStatusThenSetPasswordById() {
		User user = new User();
		user.setDocument("123456789");
		user.setName("Luis");
		user.setLastName("Espinosa");
		user.setUsername("luis3");
		user.setPassword("123456");
		user.setStatus(Status.ACTIVE.getDigit());
		
		underTest.save(user);
		
		Optional<User> expected = underTest.findByIdAndStatus(user.getId(), 
				List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()));
		assertThat(expected.isPresent()).isTrue();
		
		int modified = underTest.setPasswordById("12345678", expected.get().getId());
		assertThat(modified).isEqualTo(1);
	}
	
	
}
