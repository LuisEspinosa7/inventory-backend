/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The Interface CategoryRepository.
 * 
 * @author Luis Espinosa
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	/**
	 * Find by name and status.
	 *
	 * @param name the name
	 * @param status the status
	 * @return the optional
	 */
	Optional<Category> findByNameAndStatus(String name, int status);
	
	/**
	 * Find by id and status.
	 *
	 * @param id the id
	 * @param status the status
	 * @return the optional
	 */
	Optional<Category> findByIdAndStatus(Long id, int status);
	
	/**
	 * Find by terms containing.
	 *
	 * @param searchTerm the search term
	 * @param states the states
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(concat('%', concat(?1, '%'))) AND c.status IN(?2) ORDER BY c.id DESC")
	Page<Category> findByTermsContaining(String searchTerm, List<Integer> status, Pageable pageable);
	
	
	/**
	 * Sets the status by id.
	 *
	 * @param status the status
	 * @param id the id
	 */
	@Modifying
	@Query("update Category c SET c.status =:status WHERE c.id =:id")
	int setStatusById(@Param("status") Integer status, @Param("id") Long id);

	
	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @return the list
	 */
	List<Category> findByStatus(Integer status);
	
	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<Category> findByStatus(Integer status, Pageable pageable);
}
