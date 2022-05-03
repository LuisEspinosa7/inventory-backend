/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.Set;

import javax.validation.constraints.NotNull;
import com.lsoftware.inventory.role.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class UserSimpleDTO.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSimpleDTO {
	
	/** The id. */
	@NotNull(message = "User Id {error.NotNull}")
	private Long id;
	
	/** The document. */
	private String document;
	
	/** The name. */
	private String name;
	
	/** The lastName. */
	private String lastName;
	
	/** The username. */
	private String username;
	
	/** The password. */
	private String password;
	
	/** The status. */
	private int status;

	/** The roles. */
	private Set<Role> roles;
	
}
