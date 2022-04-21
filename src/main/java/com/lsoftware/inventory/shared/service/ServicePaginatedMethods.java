/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.service;

import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;

/**
 * The Interface ServicePaginatedMethods.
 * 
 * @author Luis Espinosa
 *
 * @param <T> the generic type
 */
public interface ServicePaginatedMethods<T> {
	
	/**
	 * Find all.
	 *
	 * @param pageable the pageable
	 * @return the page
	 */
	ResponsePaginationAndSortDTO<T> findAll(RequestPaginationAndSortDTO pageAndSort);
	
	/**
	 * Find by term containing.
	 *
	 * @param term the term
	 * @param pageable the pageable
	 * @return the page
	 */
	ResponsePaginationAndSortDTO<T> findByTermContaining(String searchTerm, RequestPaginationAndSortDTO pageAndSort);
	
}
