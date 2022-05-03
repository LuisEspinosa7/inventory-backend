/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.lsoftware.inventory.user.UserSimpleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class MovementDTO.
 * 
 * @author Luis Espinosa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovementDTO {
	
	/** The id. */
	private Long id;
	
	/** The type. */
	@NotNull(message = "Movement type {error.NotNull}")
	@NotEmpty(message = "Movement type {error.NotEmpty}")
	private String type;
	
	/** The category. */
	@NotNull(message = "User {error.NotNull}")
	private UserSimpleDTO user;
	
	/** The code. */
	private String code;
	
	/** The details. */
	@NotNull(message = "Movement details {error.NotNull}")
	private List<MovementDetailDTO> details;

}
