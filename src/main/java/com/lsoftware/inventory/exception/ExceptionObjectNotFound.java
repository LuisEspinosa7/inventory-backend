/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class ExceptionObjectNotFound.
 * 
 * @author Luis Espinosa
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExceptionObjectNotFound extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7135726265646962694L;


	/**
	 * Instantiates a new exception object not found.
	 *
	 * @param exception the exception
	 */
	public ExceptionObjectNotFound(String exception) {
		super(exception);
	}
	
}

