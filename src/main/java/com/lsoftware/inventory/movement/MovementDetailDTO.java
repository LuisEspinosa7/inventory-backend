/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import javax.validation.constraints.NotNull;

import com.lsoftware.inventory.product.ProductSimpleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class MovementDetailDTO.
 * 
 * @author Luis Espinosa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovementDetailDTO {
	
	/** The id. */
	private Long id;
	
	/** The product. */
	@NotNull(message = "Product {error.NotNull}")
	private ProductSimpleDTO product;
	
	/** The quantity. */
	@NotNull(message = "Quantity {error.NotNull}")
	private int quantity;
	
}
