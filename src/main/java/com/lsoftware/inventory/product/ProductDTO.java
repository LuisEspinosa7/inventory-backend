/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.lsoftware.inventory.category.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Class ProductDTO.
 * 
 * @author Luis Espinosa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
	
	/** The id. */
	@NotNull(message = "Id {error.NotNull}")
	private Long id;
	
	/** The name. */
	@NotNull(message = "Name {error.NotNull}")
	@NotEmpty(message = "Name {error.NotEmpty}")
	@Size(min=3, message="Name {error.MinSizeA} 3 {error.MinSizeB}")
	private String name;
	
	/** The price. */
	@NotNull(message = "Price {error.NotNull}")
	@NotEmpty(message = "Price {error.NotEmpty}")
	private String price;
	
	/** The quantity. */
	@NotNull(message = "Quantity {error.NotNull}")
	private int quantity;
	
	/** The category. */
	@NotNull(message = "Category {error.NotNull}")
	private Category category;
	
	/** The status. */
	@NotNull(message = "Status {error.NotNull}")
	private int status;

}
