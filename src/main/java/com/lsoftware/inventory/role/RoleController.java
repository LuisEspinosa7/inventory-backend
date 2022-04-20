/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.role;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsoftware.inventory.shared.api.ApiCustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * The Class RoleController.
 * 
 * @author Luis Espinosa
 */
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

	/** The role service. */
	private RoleService roleService;

	/**
	 * Instantiates a new role controller.
	 *
	 * @param roleService the role service
	 */
	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * List.
	 *
	 * @return the response entity
	 */

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@Operation(summary = "Get the available roles")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Finnd the roles list", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RoleDTO.class)) }) })
	@GetMapping
	public ResponseEntity<ApiCustomResponse> list() {
		LOG.info("method: list");

		List<RoleDTO> roles = roleService.list();
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Roles list").data(roles).build();

		return ResponseEntity.ok(response);
	}

}
