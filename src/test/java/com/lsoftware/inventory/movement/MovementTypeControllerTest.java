/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
 * The Class MovementTypeControllerTest.
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class MovementTypeControllerTest {
	
	/** The movement type service. */
	@MockBean
	private MovementTypeService movementTypeService;
	
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * It should list available movement types.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@WithMockUser(username = "valen4", password = "123456", roles = "SUPERVISOR")	
	void itShouldListAvailableMovementTypes() throws Exception {
		List<MovementTypeDTO> expectedData = List.of(
				new MovementTypeDTO("INPUT", "DESCRIPTION"),
				new MovementTypeDTO("OUTPUT", "DESCRIPTION")
		);
		
		BDDMockito.given(movementTypeService.list())
			.willReturn(expectedData);
		
		MvcResult result = mockMvc.perform(get("/api/v1/movementTypes"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpectAll(MockMvcResultMatchers.jsonPath("$.message").value("Movement Types list"))
				.andExpect(MockMvcResultMatchers.jsonPath("$..data.length()").value(2))
				.andReturn();
		
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();
	}

}
