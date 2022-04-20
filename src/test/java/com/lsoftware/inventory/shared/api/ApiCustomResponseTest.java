/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * The Class ApiErrorTest.
 * 
 * @author Luis Espinosa
 */
class ApiCustomResponseTest {

	/**
	 * It S hould configure api error.
	 */
	@Test
	void itSHouldConfigureApiError() {
		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(HttpStatus.FORBIDDEN.toString().split(" ")[0]))
				.message(HttpStatus.FORBIDDEN.name()).path("").build();
		
		assertThat(error.getStatus()).isEqualTo(403);
		
		assertThat(error.getMessage()).isEqualTo(HttpStatus.FORBIDDEN.name());
		assertThat(error.getPath()).isEqualTo("");
		assertThat(error.getTimestamp()).isNotNull();
	}

}
