/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class CategoryDTO.
 * 
 * @author Luis Espinosa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {

	/** The id. */
	@NotNull(message = "Id {error.NotNull}")
	private Long id;
	
	/** The name. */
	@NotNull(message = "Name {error.NotNull}")
	@NotEmpty(message = "Name {error.NotEmpty}")
	@Size(min=3, message="Name {error.MinSizeA} 3 {error.MinSizeB}")
	private String name;
	
	/** The status. */
	@NotNull(message = "Name {error.NotNull}")
	private int status;

}
