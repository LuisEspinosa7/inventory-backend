/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The Interface ProductRepository.
 * 
 * @author Luis Espinosa
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @return the list
	 */
	List<Product> findByStatus(Integer status);
	
	/**
	 * Find by name and status.
	 *
	 * @param name the name
	 * @param status the status
	 * @return the optional
	 */
	@Query("SELECT p FROM Product p WHERE p.name = ?1 AND p.status IN(?2)")
	Optional<Product> findByNameAndStatus(String name, List<Integer> status); // Create
	
	/**
	 * Find by id and status.
	 *
	 * @param id the id
	 * @param status the status
	 * @return the optional
	 */
	@Query("SELECT p FROM Product p WHERE p.id = ?1 AND p.status IN(?2)")
	Optional<Product> findByIdAndStatus(Long id, List<Integer> status); // Update
	
	/**
	 * Sets the status by id.
	 *
	 * @param status the status
	 * @param id the id
	 * @return the int
	 */
	@Modifying
	@Query("update Product p SET p.status =:status WHERE p.id =:id")
	int setStatusById(@Param("status") Integer status, @Param("id") Long id); // Delete

	
	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query("SELECT p FROM Product p WHERE p.status IN(?1) ORDER BY p.id DESC")
	Page<Product> findByStatus(List<Integer> status, Pageable pageable); // List no search term
	
	
	/**
	 * Find by terms containing.
	 *
	 * @param searchTerm the search term
	 * @param status the status
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(concat('%', concat(?1, '%'))) AND p.status IN(?2) ORDER BY p.id DESC")
	Page<Product> findByTermsContaining(String searchTerm, List<Integer> status, Pageable pageable); // List WITH search term
	
	
	/**
	 * Find by category id.
	 *
	 * @param id the id
	 * @return the long
	 */
	@Query("SELECT COUNT(p) FROM Product p WHERE p.category.id=?1")
    long countByCategoryId(Long id);
	
}
