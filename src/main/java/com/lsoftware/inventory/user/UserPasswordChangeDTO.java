/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Class UserPasswordChangeDTO.
 * 
 * @author Luis Espinosa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPasswordChangeDTO {
	
	/** The username. */
	@NotNull(message = "Username {error.NotNull}")
	@NotEmpty(message = "Username {error.NotEmpty}")
	@Size(min=3, message="Username {error.MinSizeA} 3 {error.MinSizeB}")
	private String username;
	
	/** The Old password. */
	@NotNull(message = "Old password {error.NotNull}")
	@NotEmpty(message = "Old password {error.NotEmpty}")
	@Size(min=3, message="Old password {error.MinSizeA} 3 {error.MinSizeB}")
	private String oldPassword;
	
	/** The New password. */
	@NotNull(message = "New password {error.NotNull}")
	@NotEmpty(message = "New password {error.NotEmpty}")
	@Size(min=3, message="New password {error.MinSizeA} 3 {error.MinSizeB}")
	private String newPassword;

}
