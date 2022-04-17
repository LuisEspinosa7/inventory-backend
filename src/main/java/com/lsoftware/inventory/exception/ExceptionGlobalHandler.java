/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lsoftware.inventory.shared.api.ApiError;

/**
 * The Class GlobalExceptionHandler.
 * 
 * @author Luis Espinosa
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class ExceptionGlobalHandler extends ResponseEntityExceptionHandler {
	
	
	/**
	 * Access denied exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public final ResponseEntity<ApiError> accessDeniedException(AccessDeniedException ex) {
		
		HttpStatus httpStatus = HttpStatus.FORBIDDEN;

		ApiError error = new ApiError.ApiErrorBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.error(httpStatus.name()).message("Access Denied").path("").build();

		return new ResponseEntity<>(error, httpStatus);
	}

}