/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * To string.
 * 
 * @author Luis Espinosa
 *
 * @return the java.lang. string
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponsePaginationAndSortDTO<T> {

	/** The result. */
	List<T> result;
	
	/** The current page. */
	int currentPage;
	
	/** The total items. */
	long totalItems;
	
	/** The total pages. */
	int totalPages;

}
