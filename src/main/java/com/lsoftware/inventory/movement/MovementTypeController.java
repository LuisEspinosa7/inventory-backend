/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsoftware.inventory.role.RoleDTO;
import com.lsoftware.inventory.shared.api.ApiCustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * The Class MovementTypeController.
 * 
 * @author Luis Espinosa
 */
@RestController
@RequestMapping("/api/v1/movementTypes")
public class MovementTypeController {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(MovementTypeController.class);
	
	/** The movement type service. */
	private MovementTypeService movementTypeService;
	
	/**
	 * Instantiates a new movement type controller.
	 *
	 * @param movementTypeService the movement type service
	 */
	public MovementTypeController(MovementTypeService movementTypeService) {
		this.movementTypeService = movementTypeService;
	}
	
	/**
	 * List.
	 *
	 * @return the response entity
	 */
	@PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
	@Operation(summary = "Get the available movement types")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Find the movement types list", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RoleDTO.class)) }) })
	@GetMapping
	public ResponseEntity<ApiCustomResponse> list() {
		LOG.info("method: list");

		List<MovementTypeDTO> types = movementTypeService.list();
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Movement Types list").data(types).build();

		return ResponseEntity.ok(response);
	}

}
