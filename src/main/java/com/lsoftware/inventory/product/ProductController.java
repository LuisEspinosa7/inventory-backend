/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

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
 * The Class ProductController.
 * 
 * @author Luis Espinosa
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	/** The user service. */
	private ProductService productService;


	/**
	 * Instantiates a new product controller.
	 *
	 * @param productService the product service
	 */
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	
	/**
	 * Creates the.
	 *
	 * @param productDTO the product DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Create a new Product")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Create the new product", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping
	public ResponseEntity<ApiCustomResponse> create(@Valid @RequestBody ProductDTO productDTO) {
		LOG.info("method: create");

		ProductDTO user = productService.add(productDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Product created")
				.data(user).build();

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Update.
	 *
	 * @param productDTO the product DTO
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Update a Product")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Update a product", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PutMapping
	public ResponseEntity<ApiCustomResponse> update(@Valid @RequestBody ProductDTO productDTO) {
		LOG.info("method: update");

		ProductDTO user = productService.update(productDTO);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Product updated")
				.data(user).build();

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Delete a Product")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Delete a product", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiCustomResponse> delete(@PathVariable("id") @NotBlank @Size(min = 1, max = 10) Long id) {
		LOG.info("method: delete");

		productService.delete(id);
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Product deleted").build();

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Paginate.
	 *
	 * @param pageAndSort the page and sort
	 * @return the response entity
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "Paginate the Products")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Paginates the availbale products", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@PostMapping("/paginate")
	public ResponseEntity<ApiCustomResponse> paginate(@Valid @RequestBody RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: paginate");

		ResponsePaginationAndSortDTO<ProductDTO> results;
		if (pageAndSort.getSearchTerm() == null)
			results = productService.findAll(pageAndSort);
		else
			results = productService.findByTermContaining(pageAndSort.getSearchTerm(), pageAndSort);

		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Products Paginated")
				.data(results).build();

		return ResponseEntity.ok(response);
	}
	
	
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
	@Operation(summary = "List the available products")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List the available products", content = {
			@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiCustomResponse.class)) }) })
	@GetMapping()
	public ResponseEntity<ApiCustomResponse> listAll() {
		LOG.info("method: listAll");

		List<ProductDTO> results = productService.list();
		ApiCustomResponse response = new ApiCustomResponse.ApiResponseBuilder(200).message("Products List").data(results).build();

		return ResponseEntity.ok(response);
	}
	
}
