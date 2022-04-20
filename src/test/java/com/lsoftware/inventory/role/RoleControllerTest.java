/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


/**
 * The Class RoleControllerTest.
 * The following annotations load only a part of the context, so it is only useful 
 * for simple approaches, since we have Spring token based security, it turns out difficult.
 * @ExtendWith(SpringExtension.class)
 * @WebMvcTest(value = RoleController.class)
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {
	
	/** The Constant TOKEN_EXAMPLE. */
	private static final String TOKEN_EXAMPLE = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWlzMyIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImlhdCI6MTY1MDIyMzk3NiwiZXhwIjoxNjUxMDM1NjAwfQ.wGYgnNdA6r_8k0EYmjE0Cs_e3FSygPfaRknlmLaS4NdPBK7NeM1LC_lx0_N8A706SnoAe9uEZ-2uSIZN28klYA";
		
	/** The role service. */
	@MockBean
	private RoleService roleService; 
	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;
	

	/**
	 * It should fail with forbidden status.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void itShouldFailWithForbiddenStatus() throws Exception {
		
		mockMvc.perform(get("/api/v1/roles"))
			.andExpect(status().isForbidden());
	}
	
	
	/**
	 * It should list available roles.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "luis3", password = "123456", roles = "ADMIN")
	void itShouldListAvailableRoles() throws Exception {
	
		List<RoleDTO> expectedData = List.of(
				new RoleDTO(1L, "ADMIN", "DESCRIPTION")
		);
		
		Mockito.when(roleService.list()).thenReturn(expectedData);
		
		MvcResult result = mockMvc.perform(get("/api/v1/roles"))
			.andDo(MockMvcResultHandlers.print())
        	.andExpect(status().isOk())
        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Roles list"))
        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(1))
        	.andReturn();
		
		
		 String resultSS = result.getResponse().getContentAsString();
	     assertThat(resultSS).isNotNull();	
	}
	
	
	/**
	 * It should list available roles processing token.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void itShouldListAvailableRolesProcessingToken() throws Exception {
		
		List<RoleDTO> expectedData = List.of(
				new RoleDTO(1L, "ADMIN", "DESCRIPTION")
		);
		
		Mockito.when(roleService.list()).thenReturn(expectedData);
		
		MvcResult result = mockMvc.perform(get("/api/v1/roles")
				.header("Authorization", TOKEN_EXAMPLE))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Roles list"))
	        	.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(1))
	        	.andReturn();
			
			
			 String resultSS = result.getResponse().getContentAsString();
		     assertThat(resultSS).isNotNull();
	}
	
}
