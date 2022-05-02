/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.category.Category;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class ProductControllerTest.
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	/** The product service. */
	@MockBean
	private ProductService productService; 
	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;
	
	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * It should create new product.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "LUIS3", password = "12345678", roles = "ADMIN")
	void itShouldCreateNewProduct() throws JsonProcessingException, Exception {
		
		Mockito.when(productService.add(any())).thenReturn(getProductDTO());
		
		MvcResult result = mockMvc.perform(post("/api/v1/products/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getProductDTO())))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product created"))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	
	
	/**
	 * It should update product.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "LUIS3", password = "12345678", roles = "ADMIN")
	void itShouldUpdateProduct() throws JsonProcessingException, Exception {
		
		Mockito.when(productService.update(any())).thenReturn(getProductDTO());
		
		MvcResult result = mockMvc.perform(put("/api/v1/products/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getProductDTO())))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product updated"))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	/**
	 * It should delete product.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "LUIS3", password = "12345678", roles = "ADMIN")
	void itShouldDeleteProduct() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(delete("/api/v1/products/" + 1L )
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product deleted"))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	/**
	 * It should list paginated products no search term.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "LUIS3", password = "12345678", roles = "ADMIN")
	void itShouldListPaginatedProductsNoSearchTerm() throws Exception {
		
		RequestPaginationAndSortDTO pageAndSort = new RequestPaginationAndSortDTO();
		pageAndSort.setPage(0);
		pageAndSort.setSize(2);
		
		List<ProductDTO> expectedData = List.of(
				getProductDTO(),
				new ProductDTO(2L, "Product 1", "12000", 2, new Category(3L, "CATEGORY 3", 1), Status.ACTIVE.getDigit())
		);
		
		ResponsePaginationAndSortDTO<ProductDTO> resp 
			= new ResponsePaginationAndSortDTO<>(expectedData, 0, 
				2, 1);
		
		Mockito.when(productService.findAll(any())).thenReturn(resp);
		
		MvcResult result = mockMvc.perform(post("/api/v1/products/paginate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(pageAndSort)))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Products Paginated"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.result.length()").value(2))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}
	
	/**
	 * It should list paginated products WITH search term.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "LUIS3", password = "12345678", roles = "ADMIN")
	void itShouldListPaginatedProductsWITHSearchTerm() throws Exception {
		
		RequestPaginationAndSortDTO pageAndSort = new RequestPaginationAndSortDTO();
		pageAndSort.setPage(0);
		pageAndSort.setSize(2);
		pageAndSort.setSearchTerm("Val");
		
		List<ProductDTO> expectedData = List.of(
				getProductDTO(),
				new ProductDTO(2L, "Product 1", "12000", 2, new Category(3L, "CATEGORY 3", 1), Status.ACTIVE.getDigit())
		);
		
		ResponsePaginationAndSortDTO<ProductDTO> resp 
			= new ResponsePaginationAndSortDTO<>(expectedData, 0, 
				2, 1);
		
		Mockito.when(productService.findByTermContaining(anyString(), any())).thenReturn(resp);
		
		MvcResult result = mockMvc.perform(post("/api/v1/products/paginate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(pageAndSort)))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Products Paginated"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.result.length()").value(2))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}

	
	
	/**
	 * It should list all available.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "LUIS3", password = "12345678", roles = "ADMIN")
	void itShouldListAllAvailable() throws Exception {
		
		List<ProductDTO> expectedData = List.of(
				getProductDTO(),
				new ProductDTO(2L, "Product 1", "12000", 2, new Category(3L, "CATEGORY 3", 1), Status.ACTIVE.getDigit())
		);
		
		Mockito.when(productService.list()).thenReturn(expectedData);
		
		MvcResult result = mockMvc.perform(get("/api/v1/products/"))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Products List"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(2))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}

	
	/**
	 * Gets the product DTO.
	 *
	 * @return the product DTO
	 */
	private ProductDTO getProductDTO() {
		return new ProductDTO(1L, "Product 1", "12000", 2, new Category(1L, "CATEGORY 1", 1), Status.ACTIVE.getDigit());
	}
	
	
}
