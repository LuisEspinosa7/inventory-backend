/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.status.Status;


/**
 * The Class CategoryControllerTest.
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {
	
	/** The category service. */
	@MockBean
	private CategoryService categoryService; 
	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;
	
	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * It should list available categories.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldListAvailableCategories() throws Exception {
	
		List<CategoryDTO> expectedData = List.of(
				new CategoryDTO(1L, "CATEGORY1", Status.ACTIVE.getDigit()),
				new CategoryDTO(1L, "CATEGORY2", Status.ACTIVE.getDigit())
		);
		
		Mockito.when(categoryService.list()).thenReturn(expectedData);
		
		MvcResult result = mockMvc.perform(get("/api/v1/categories"))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Categories List"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(2))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}
	
	/**
	 * It should list paginated categories no search term.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldListPaginatedCategoriesNoSearchTerm() throws Exception {
		
		RequestPaginationAndSortDTO pageAndSort = new RequestPaginationAndSortDTO();
		pageAndSort.setPage(0);
		pageAndSort.setSize(2);
		
		List<CategoryDTO> expectedData = List.of(
				new CategoryDTO(1L, "CATEGORY1", Status.ACTIVE.getDigit()),
				new CategoryDTO(2L, "CATEGORY2", Status.ACTIVE.getDigit()),
				new CategoryDTO(3L, "CATEGORY3", Status.ACTIVE.getDigit()),
				new CategoryDTO(4L, "CATEGORY4", Status.ACTIVE.getDigit())
		);
		
		ResponsePaginationAndSortDTO<CategoryDTO> resp 
			= new ResponsePaginationAndSortDTO<>(expectedData, 2, 
				4, 2);
		
		Mockito.when(categoryService.findAll(any())).thenReturn(resp);
		
		MvcResult result = mockMvc.perform(post("/api/v1/categories/paginate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(pageAndSort)))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Categories Paginated"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(4))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}
	
	
	/**
	 * It should list paginated categories with search term.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldListPaginatedCategoriesWithSearchTerm() throws Exception {
		
		RequestPaginationAndSortDTO pageAndSort = new RequestPaginationAndSortDTO();
		pageAndSort.setPage(0);
		pageAndSort.setSize(2);
		pageAndSort.setSearchTerm("Cat");
		
		List<CategoryDTO> expectedData = List.of(
				new CategoryDTO(1L, "CATEGORY1", Status.ACTIVE.getDigit()),
				new CategoryDTO(2L, "CATEGORY2", Status.ACTIVE.getDigit()),
				new CategoryDTO(3L, "CATEGORY3", Status.ACTIVE.getDigit()),
				new CategoryDTO(4L, "CATEGORY4", Status.ACTIVE.getDigit())
		);
		
		ResponsePaginationAndSortDTO<CategoryDTO> resp 
			= new ResponsePaginationAndSortDTO<>(expectedData, 2, 
				4, 2);
		
		Mockito.when(categoryService.findByTermContaining(anyString(), any())).thenReturn(resp);
		
		MvcResult result = mockMvc.perform(post("/api/v1/categories/paginate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(pageAndSort)))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Categories Paginated"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(4))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}

}
