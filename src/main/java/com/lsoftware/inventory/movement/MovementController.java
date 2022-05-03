/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsoftware.inventory.shared.api.ApiCustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * The Class MovementController.
 * 
 * @author Luis Espinosa
 */
@RestController
@RequestMapping("/api/v1/movements")
public class MovementController {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(MovementController.class);

	/** The movement service. */
	private MovementService movementService;


	
	/**
	 * Instantiates a new movement controller.
	 *
	 * @param movementService the movement service
	 */
	public MovementController(MovementService movementService) {
		this.movementService = movementService;
	}
	
	
	/**
	 * Creates the.
	 *
	 * @param movementDTO the movement DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAuthority('ROLE_SUPERVISOR')")
	@Operation(summary = "Create a new movement")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Create the new movement", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping
	public ResponseEntity<ApiCustomResponse> create(@Valid @RequestBody MovementDTO movementDTO) {
		LOG.info("method: create");

		MovementDTO user = movementService.add(movementDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Movement created")
				.data(user).build();

		return ResponseEntity.ok(response);
	}

}
