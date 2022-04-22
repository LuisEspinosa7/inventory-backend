/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

/**
 * The Class CategoryController.
 * 
 * @author Luis Espinosa
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

	/** The category service. */
	private CategoryService categoryService;

	/**
	 * Instantiates a new category controller.
	 *
	 * @param categoryService the category service
	 */
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/**
	 * Creates the.
	 *
	 * @param categoryDTO the category DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Create a new Category")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Create the new category", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping
	public ResponseEntity<ApiCustomResponse> create(@Valid @RequestBody CategoryDTO categoryDTO) {
		LOG.info("method: create");

		CategoryDTO category = categoryService.add(categoryDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Category created")
				.data(category).build();

		return ResponseEntity.ok(response);
	}

	/**
	 * Update.
	 *
	 * @param categoryDTO the category DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Update a Category")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Update a category", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PutMapping
	public ResponseEntity<ApiCustomResponse> update(@Valid @RequestBody CategoryDTO categoryDTO) {
		LOG.info("method: update");

		CategoryDTO category = categoryService.update(categoryDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Category updated")
				.data(category).build();

		return ResponseEntity.ok(response);
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Delete a Category")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Delete a category", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiCustomResponse> delete(@PathVariable("id") @NotBlank @Size(min = 1, max = 10) Long id) {
		LOG.info("method: delete");

		categoryService.delete(id);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Category deleted").build();

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * List all.
	 *
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "List the available categories")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List the available categories", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@GetMapping()
	public ResponseEntity<ApiCustomResponse> listAll() {
		LOG.info("method: listAll");

		List<CategoryDTO> results = categoryService.list();
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Categories List").data(results).build();

		return ResponseEntity.ok(response);
	}

	
	/**
	 * Paginate.
	 *
	 * @param pageAndSort the page and sort
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Paginate the Categories")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Paginates the availbale categories", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping("/paginate")
	public ResponseEntity<ApiCustomResponse> paginate(@Valid @RequestBody RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: paginate");

		ResponsePaginationAndSortDTO<CategoryDTO> results;
		if (pageAndSort.getSearchTerm() == null)
			results = categoryService.findAll(pageAndSort);
		else
			results = categoryService.findByTermContaining(pageAndSort.getSearchTerm(), pageAndSort);

		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Categories Paginated")
				.data(results).build();

		return ResponseEntity.ok(response);
	}

}
