/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import com.lsoftware.inventory.shared.api.ApiError;

/**
 * The Class ExceptionGlobalHandlerTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class ExceptionGlobalHandlerTest {
	
	/** The under test. */
	private ExceptionGlobalHandler underTest;
	
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new ExceptionGlobalHandler();
	}
	
	/**
	 * Checks if is should join access denied exception.
	 */
	@Test
	void isShouldJoinAccessDeniedException() {
		ResponseEntity<ApiError> response = underTest.accessDeniedException(new AccessDeniedException("dummy"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

}
