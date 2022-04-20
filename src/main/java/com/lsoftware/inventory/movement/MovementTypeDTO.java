/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new movement type DTO.
 *
 * @param name the name
 * @param description the description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementTypeDTO {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	
}
