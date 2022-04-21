/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class InternalServerErrorException.
 * 
 * @author Luis Espinosa
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -990795612962216707L;

	/**
	 * Instantiates a new internal server error exception.
	 *
	 * @param exception the exception
	 */
	public InternalServerErrorException(String exception) {
		super(exception);
	}

}

