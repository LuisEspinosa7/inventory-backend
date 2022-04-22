/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To string.
 * 
 * @author Luis Espinosa
 *
 * @return the java.lang. string
 */
@Data
public class RequestPaginationAndSortDTO {

	/** The search term. */
	private String searchTerm;

	/** The page. */
	@NotNull(message = "Page {error.NotNull}")
	private int page;

	/** The size. */
	@NotNull(message = "Size {error.NotNull}")
	private int size;

	/**
	 * Instantiates a new request pagination and sort DTO.
	 */
	public RequestPaginationAndSortDTO() {

	}

	/**
	 * Instantiates a new request pagination and sort DTO.
	 *
	 * @param searchTerm the search term
	 * @param page the page
	 * @param size the size
	 */
	public RequestPaginationAndSortDTO(String searchTerm, int page, int size) {
		this.searchTerm = searchTerm;
		this.page = page;
		this.size = size;
	}

}
