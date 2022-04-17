/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface UsuarioRepository.
 * 
 * @author Luis Espinosa
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * Find by username.
	 *
	 * @param username the username
	 * @return the optional
	 */
	Optional<User> findByUsername(String username);

}
