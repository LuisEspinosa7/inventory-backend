/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.lsoftware.inventory.shared.api.ApiCustomResponse;

/**
 * The Class ExceptionGlobalHandlerTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class ExceptionGlobalHandlerTest {
	
	/** The under test. */
	private ExceptionGlobalHandler underTest;
	
	/** The http headers. */
	@Mock
	private HttpHeaders httpHeaders;
	
	/** The web request. */
	@Mock
	private WebRequest webRequest;
	
	/** The parameter. */
	@Mock
	private MethodParameter parameter;
	
	/** The binding result. */
	@Mock
	private BindingResult bindingResult;
	
	
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
	void isShouldHandleAccessDeniedException() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleAccessDeniedException(new AccessDeniedException("dummy"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	/**
	 * Checks if is should handle internal server error exception.
	 */
	@Test
	void isShouldHandleInternalServerErrorException() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleInternalServerErrorException(new ExceptionInternalServerError("dummy"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Checks if is should value not permitted exception with message.
	 */
	@Test
	void isShouldValueNotPermittedExceptionWithMessage() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleValueNotPermittedException(new ExceptionValueNotPermitted("dummy"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Checks if is should value not permitted exception no message.
	 */
	@Test
	void isShouldValueNotPermittedExceptionNoMessage() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleValueNotPermittedException(new ExceptionValueNotPermitted(""));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Checks if is should handle object not found exception with message.
	 */
	@Test
	void isShouldHandleObjectNotFoundExceptionWithMessage() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleObjectNotFoundException(new ExceptionObjectNotFound("dummy"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Checks if is should handle object not found exception no message.
	 */
	@Test
	void isShouldHandleObjectNotFoundExceptionNoMessage() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleObjectNotFoundException(new ExceptionObjectNotFound(""));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	/**
	 * Checks if is should handle all exceptions.
	 */
	@Test
	void isShouldHandleAllExceptions() {
		ResponseEntity<ApiCustomResponse> response = underTest.handleAllExceptions(new Exception("dummy"));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Checks if is should handle method argument not valid.
	 */
	@Test
	void isShouldHandleMethodArgumentNotValid() {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		
		ResponseEntity<Object> response = underTest.handleMethodArgumentNotValid(new MethodArgumentNotValidException(parameter, bindingResult),
				httpHeaders, httpStatus, webRequest);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}
