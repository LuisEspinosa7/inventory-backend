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
class ApiErrorTest {

	/**
	 * It S hould configure api error.
	 */
	@Test
	void itSHouldConfigureApiError() {
		ApiError error = new ApiError.ApiErrorBuilder(Integer.valueOf(HttpStatus.FORBIDDEN.toString().split(" ")[0]))
				.error(HttpStatus.FORBIDDEN.name()).message("Access Denied").path("").build();
		
		assertThat(error.getStatus()).isEqualTo(403);
		
		assertThat(error.getMessage()).isEqualTo("Access Denied");
		assertThat(error.getPath()).isEqualTo("");
		assertThat(error.getTimestamp()).isNotNull();
		assertThat(error.getError()).isEqualTo(HttpStatus.FORBIDDEN.name());
	}

}
