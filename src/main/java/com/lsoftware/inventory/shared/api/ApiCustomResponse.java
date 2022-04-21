/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.api;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Instantiates a new api error builder.
 * 
 * @author Luis Espinosa
 */
public class ApiCustomResponse {

	/** The timestamp. */
	private final String timestamp;

	/** The status. */
	private final int status;

	/** The message. */
	private final String message;

	/** The path. */
	private final String path;
	
	/** The data. */
	private final Object data;
	
	/** The validation errors. */
	private Map<String, String> validationErrors;

	/**
	 * Instantiates a new api error.
	 *
	 * @param builder the builder
	 */
	private ApiCustomResponse(ApiResponseBuilder builder) {
		this.timestamp = String.valueOf(LocalDateTime.now());
		this.status = builder.status;
		this.message = builder.message;
		this.path = builder.path;
		this.data = builder.data;
		this.validationErrors = builder.validationErrors;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * Gets the validation errors.
	 *
	 * @return the validation errors
	 */
	public Map<String, String> getValidationErrors() {
		return validationErrors;
	}

	/**
	 * The Class ApiErrorBuilder.
	 */
	public static class ApiResponseBuilder {
		
		/** The status. */
		private final int status;
		
		/** The message. */
		private String message;
		
		/** The path. */
		private String path;
		
		/** The data. */
		private Object data;
		
		/** The validation errors. */
		private Map<String, String> validationErrors;

		/**
		 * Instantiates a new api error builder.
		 *
		 * @param status the status
		 */
		public ApiResponseBuilder(int status) {
			this.status = status;
		}

		/**
		 * Phone.
		 *
		 * @param message the message
		 * @return the api error builder
		 */
		public ApiResponseBuilder message(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Address.
		 *
		 * @param path the path
		 * @return the api error builder
		 */
		public ApiResponseBuilder path(String path) {
			this.path = path;
			return this;
		}
		
		/**
		 * Data.
		 *
		 * @param data the data
		 * @return the api response builder
		 */
		public ApiResponseBuilder data(Object data) {
			this.data = data;
			return this;
		}
		
		/**
		 * Data.
		 *
		 * @param validationErrors the validation errors
		 * @return the api response builder
		 */
		public ApiResponseBuilder validationErrors(Map<String, String> validationErrors) {
			this.validationErrors = validationErrors;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the api error
		 */
		public ApiCustomResponse build() {
			ApiCustomResponse user = new ApiCustomResponse(this);
			return user;
		}

	}

}
