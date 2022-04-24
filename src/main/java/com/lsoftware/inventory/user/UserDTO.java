/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.lsoftware.inventory.role.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Class UserDTO.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
	
	/** The id. */
	@NotNull(message = "Id {error.NotNull}")
	private Long id;
	
	/** The document. */
	@NotNull(message = "Name {error.NotNull}")
	@NotEmpty(message = "Name {error.NotEmpty}")
	@Size(min=3, message="Name {error.MinSizeA} 3 {error.MinSizeB}")
	private String document;
	
	/** The name. */
	@NotNull(message = "Name {error.NotNull}")
	@NotEmpty(message = "Name {error.NotEmpty}")
	@Size(min=3, message="Name {error.MinSizeA} 3 {error.MinSizeB}")
	private String name;
	
	/** The lastName. */
	@NotNull(message = "LastName {error.NotNull}")
	@NotEmpty(message = "LastName {error.NotEmpty}")
	@Size(min=3, message="LastName {error.MinSizeA} 3 {error.MinSizeB}")
	private String lastName;
	
	/** The username. */
	@NotNull(message = "Username {error.NotNull}")
	@NotEmpty(message = "Username {error.NotEmpty}")
	@Size(min=3, message="Username {error.MinSizeA} 3 {error.MinSizeB}")
	private String username;
	
	/** The password. */
	@NotNull(message = "Password {error.NotNull}")
	@NotEmpty(message = "Password {error.NotEmpty}")
	@Size(min=3, message="Password {error.MinSizeA} 3 {error.MinSizeB}")
	private String password;
	
	/** The status. */
	@NotNull(message = "Status {error.NotNull}")
	private int status;

	/** The roles. */
	@NotNull(message = "Roles {error.NotNull}")
	private Set<Role> roles;
}
