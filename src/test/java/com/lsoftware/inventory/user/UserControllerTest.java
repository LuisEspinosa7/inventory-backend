/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

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
import com.lsoftware.inventory.role.Role;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class UserControllerTest.
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
	
	/** The user service. */
	@MockBean
	private UserService userService; 
	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;
	
	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;


	/**
	 * It should create new user.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldCreateNewUser() throws JsonProcessingException, Exception {
		
		Mockito.when(userService.add(any())).thenReturn(getUserDTO());
		
		MvcResult result = mockMvc.perform(post("/api/v1/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getUserDTO())))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User created"))
	        	//.andExpect(MockMvcResultMatchers.jsonPath("$.data").value(getUserDTO()))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	/**
	 * It should update user.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldUpdateUser() throws JsonProcessingException, Exception {
		
		Mockito.when(userService.update(any())).thenReturn(getUserDTO());
		
		MvcResult result = mockMvc.perform(put("/api/v1/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getUserDTO())))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User updated"))
	        	//.andExpect(MockMvcResultMatchers.jsonPath("$.data").value(getUserDTO()))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	/**
	 * It should delete user.
	 *
	 * @throws JsonProcessingException the json processing exception
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldDeleteUser() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(delete("/api/v1/users/" + 1L )
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User deleted"))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	/**
	 * It should list paginated users no search term.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldListPaginatedUsersNoSearchTerm() throws Exception {
		
		RequestPaginationAndSortDTO pageAndSort = new RequestPaginationAndSortDTO();
		pageAndSort.setPage(0);
		pageAndSort.setSize(2);
		
		List<UserDTO> expectedData = List.of(
				getUserDTO(),
				new UserDTO(2L, "4534534534", "Valentina", "Espinosa", "VALEN4", "123456", Status.ACTIVE.getDigit(), 
						Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")))
		);
		
		ResponsePaginationAndSortDTO<UserDTO> resp 
			= new ResponsePaginationAndSortDTO<>(expectedData, 0, 
				2, 1);
		
		Mockito.when(userService.findAll(any())).thenReturn(resp);
		
		MvcResult result = mockMvc.perform(post("/api/v1/users/paginate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(pageAndSort)))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Users Paginated"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.result.length()").value(2))
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
		pageAndSort.setSearchTerm("Val");
		
		List<UserDTO> expectedData = List.of(
				getUserDTO(),
				new UserDTO(2L, "4534534534", "Valentina", "Espinosa", "VALEN4", "123456", Status.ACTIVE.getDigit(), 
						Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")))
		);
		
		ResponsePaginationAndSortDTO<UserDTO> resp 
			= new ResponsePaginationAndSortDTO<>(expectedData, 0, 
				2, 1);
		
		Mockito.when(userService.findByTermContaining(anyString(), any())).thenReturn(resp);
		
		MvcResult result = mockMvc.perform(post("/api/v1/users/paginate")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(pageAndSort)))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Users Paginated"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.result.length()").value(2))
        	.andReturn();
		
		
		 String response = result.getResponse().getContentAsString();
	     assertThat(response).isNotNull();	
	}
	
	
	
	
	
	@Test
	@WithMockUser(username = "LUIS3", password = "123456", roles = "ADMIN")
	void itShouldUpdateUserPassword() throws JsonProcessingException, Exception {
		
		MvcResult result = mockMvc.perform(put("/api/v1/users/changePassword/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getUserPasswordChangeDTO())))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User password updated"))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	
	/**
	 * Gets the user DTO.
	 *
	 * @return the user DTO
	 */
	private UserDTO getUserDTO() {
		return new UserDTO(1L, "123456789", "Luis", "Espinosa", "luis3", "123456", Status.ACTIVE.getDigit(), 
				Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")));
	}
	
	
	/**
	 * Gets the user password change DTO.
	 *
	 * @return the user password change DTO
	 */
	private UserPasswordChangeDTO getUserPasswordChangeDTO() {
		return new UserPasswordChangeDTO("LUIS3", "123456", "12345678");
	}
	

}
