/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class ProductSimpleDTO.
 * 
 * @author Luis Espinosa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSimpleDTO {
	
	/** The id. */
	@NotNull(message = "Id {error.NotNull}")
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The price. */
	private String price;
	
	/** The quantity. */
	private int quantity;
	
	/** The status. */
	private int status;

}
