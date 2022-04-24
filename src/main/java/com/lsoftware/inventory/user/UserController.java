/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsoftware.inventory.shared.api.ApiCustomResponse;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

// TODO: Auto-generated Javadoc
/**
 * The Class UserController.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	/** The user service. */
	private UserService userService;


	/**
	 * Instantiates a new user controller.
	 *
	 * @param userService the user service
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	/**
	 * Creates the.
	 *
	 * @param userDTO the user DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@Operation(summary = "Create a new User")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Create the new user", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping
	public ResponseEntity<ApiCustomResponse> create(@Valid @RequestBody UserDTO userDTO) {
		LOG.info("method: create");

		UserDTO user = userService.add(userDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("User created")
				.data(user).build();

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Update.
	 *
	 * @param userDTO the user DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@Operation(summary = "Update a User")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Update a user", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PutMapping
	public ResponseEntity<ApiCustomResponse> update(@Valid @RequestBody UserDTO userDTO) {
		LOG.info("method: update");

		UserDTO user = userService.update(userDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("User updated")
				.data(user).build();

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@Operation(summary = "Delete a User")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Delete a user", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiCustomResponse> delete(@PathVariable("id") @NotBlank @Size(min = 1, max = 10) Long id) {
		LOG.info("method: delete");

		userService.delete(id);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("User deleted").build();

		return ResponseEntity.ok(response);
	}
	
	
	
	/**
	 * Paginate.
	 *
	 * @param pageAndSort the page and sort
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	@Operation(summary = "Paginate the Users")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Paginates the availbale users", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping("/paginate")
	public ResponseEntity<ApiCustomResponse> paginate(@Valid @RequestBody RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: paginate");

		ResponsePaginationAndSortDTO<UserDTO> results;
		if (pageAndSort.getSearchTerm() == null)
			results = userService.findAll(pageAndSort);
		else
			results = userService.findByTermContaining(pageAndSort.getSearchTerm(), pageAndSort);

		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Users Paginated")
				.data(results).build();

		return ResponseEntity.ok(response);
	}

}
