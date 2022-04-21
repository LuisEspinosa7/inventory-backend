/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class ExceptionValueNotPermitted.
 * 
 * @author Luis Espinosa
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionValueNotPermitted extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 452382267875788250L;

	
	/**
	 * Instantiates a new exception value not permitted.
	 *
	 * @param exception the exception
	 */
	public ExceptionValueNotPermitted(String exception) {
		super(exception);
	}
	
}
