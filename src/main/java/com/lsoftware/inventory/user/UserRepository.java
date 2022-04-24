/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	Optional<User> findByUsername(String username); // Authentication
	
	
	/**
	 * Find by document and username and status.
	 *
	 * @param document the document
	 * @param username the username
	 * @param states the states
	 * @return the optional
	 */
	@Query("SELECT u FROM User u WHERE (u.document = ?1 OR u.username = ?2) AND u.status IN(?3)")
	Optional<User> findByDocumentAndUsernameAndStatus(String document, String username, List<Integer> status); // Create
	
	
	/**
	 * Find by id and status.
	 *
	 * @param id the id
	 * @param status the status
	 * @return the optional
	 */
	@Query("SELECT u FROM User u WHERE u.id = ?1 AND u.status IN(?2)")
	Optional<User> findByIdAndStatus(Long id, List<Integer> status); // Update
	
	
	/**
	 * Sets the state by id.
	 *
	 * @param status the status
	 * @param id the id
	 */
	@Modifying
	@Query("update User u SET u.status =:status WHERE u.id =:id")
	int setStatusById(@Param("status") Integer status, @Param("id") Long id);
	
	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query("SELECT u FROM User u WHERE u.status IN(?1) ORDER BY u.id DESC")
	Page<User> findByStatus(List<Integer> status, Pageable pageable); // List no search term
	
	/**
	 * Find by terms containing.
	 *
	 * @param searchTerm the search term
	 * @param status the status
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query("SELECT u FROM User u WHERE ( LOWER(u.name) LIKE LOWER(concat('%', concat(?1, '%'))) OR "
			+ "LOWER(u.lastName) LIKE LOWER(concat('%', concat(?1, '%'))) ) AND u.status IN(?2) ORDER BY u.id DESC")
	Page<User> findByTermsContaining(String searchTerm, List<Integer> status, Pageable pageable); // List WITH search term

}
