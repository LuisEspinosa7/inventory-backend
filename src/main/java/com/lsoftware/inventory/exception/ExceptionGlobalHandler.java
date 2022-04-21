/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lsoftware.inventory.shared.api.ApiCustomResponse;

/**
 * The Class GlobalExceptionHandler.
 * 
 * @author Luis Espinosa
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class ExceptionGlobalHandler extends ResponseEntityExceptionHandler {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionGlobalHandler.class);
	
	
	/**
	 * Access denied exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public final ResponseEntity<ApiCustomResponse> handleAccessDeniedException(AccessDeniedException ex) {
		LOG.error("method: handleAccessDeniedException");
		
		HttpStatus httpStatus = HttpStatus.FORBIDDEN;

		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.message(httpStatus.name()).path("").build();

		return new ResponseEntity<>(error, httpStatus);
	}

	
	/**
	 * Handle internal server error exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(ExceptionInternalServerError.class)
	public final ResponseEntity<ApiCustomResponse> handleInternalServerErrorException(ExceptionInternalServerError ex) {
		LOG.error("method: handleInternalServerErrorException");
		
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.message(httpStatus.name()).path("").build();

		return new ResponseEntity<>(error, httpStatus);
	}
	
	
	/**
	 * Handle value not permitted exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(ExceptionValueNotPermitted.class)
	public final ResponseEntity<ApiCustomResponse> handleValueNotPermittedException(ExceptionValueNotPermitted ex) {
		LOG.error("method: handleValueNotPermittedException");
		
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.message(!ex.getMessage().isEmpty() ? ex.getMessage() : httpStatus.name()).path("").build();

		return new ResponseEntity<>(error, httpStatus);
	}
	
	/**
	 * Handle object not found exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(ExceptionObjectNotFound.class)
	public final ResponseEntity<ApiCustomResponse> handleObjectNotFoundException(ExceptionObjectNotFound ex) {
		LOG.error("method: handleObjectNotFoundException");
		
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;

		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.message(!ex.getMessage().isEmpty() ? ex.getMessage() : httpStatus.name()).path("").build();

		return new ResponseEntity<>(error, httpStatus);
	}
	
	/**
	 * Handle all exceptions.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ApiCustomResponse> handleAllExceptions(Exception e) {
		LOG.error("method: handleAllExceptions");
		
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.message("General Error").path("").build();

		return new ResponseEntity<>(error, httpStatus);
	}
	
	
	/**
	 * Handle method argument not valid.
	 *
	 * @param ex the ex
	 * @param headers the headers
	 * @param status the status
	 * @param request the request
	 * @return the response entity
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		LOG.error("method: handleMethodArgumentNotValid");
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		
		
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		ApiCustomResponse error = new ApiCustomResponse.ApiResponseBuilder(Integer.valueOf(httpStatus.toString().split(" ")[0]))
				.message(httpStatus.name()).validationErrors(errors).path("").build();

		return new ResponseEntity<>(error, httpStatus);	
	}
	
}
