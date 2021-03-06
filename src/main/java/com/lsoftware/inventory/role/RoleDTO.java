/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Instantiates a new role DTO.
 * 
 * @author Luis Espinosa
 *
 * @param id the id
 * @param name the name
 * @param description the description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
	
	/** The id. */
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
}
