/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.product.ProductSimpleDTO;
import com.lsoftware.inventory.user.UserSimpleDTO;

/**
 * The Class MovementControllerTest.
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTest {

	/** The movement service. */
	@MockBean
	private MovementService movementService; 
	
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
	@WithMockUser(username = "VALEN4", password = "123456", roles = "SUPERVISOR")
	void itShouldCreateNewMovement() throws JsonProcessingException, Exception {
		
		Mockito.when(movementService.add(any())).thenReturn(getMovementDTO("INPUT", 5));
		
		MvcResult result = mockMvc.perform(post("/api/v1/movements/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getMovementDTO("INPUT", 5))))
				.andDo(MockMvcResultHandlers.print())
	        	.andExpect(status().isOk())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Movement created"))
	        	.andReturn();
			
		String response = result.getResponse().getContentAsString();
		assertThat(response).isNotNull();	
	}
	
	/**
	 * Gets the movement DTO.
	 *
	 * @param type the type
	 * @param quantity the quantity
	 * @return the movement DTO
	 */
	private MovementDTO getMovementDTO(String type, int quantity) {
		UserSimpleDTO user = new UserSimpleDTO();
		user.setId(1L);
		
		ProductSimpleDTO product = new ProductSimpleDTO();
		product.setId(1L);
		
		MovementDetailDTO detail = new MovementDetailDTO();
		detail.setProduct(product);
		detail.setQuantity(quantity);
		
		return new MovementDTO(0L, type, user, "", List.of(detail));
	}

}
