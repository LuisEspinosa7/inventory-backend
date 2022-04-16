/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.model;

import java.time.LocalDateTime;

/**
 * Instantiates a new api error builder.
 * 
 * @author Luis Espinosa
 */
public class ApiError {

	/** The timestamp. */
	private final String timestamp;

	/** The status. */
	private final int status;

	/** The error. */
	private final String error;

	/** The message. */
	private final String message;

	/** The path. */
	private final String path;

	/**
	 * Instantiates a new api error.
	 *
	 * @param builder the builder
	 */
	private ApiError(ApiErrorBuilder builder) {
		this.timestamp = String.valueOf(LocalDateTime.now());
		this.status = builder.status;
		this.error = builder.error;
		this.message = builder.message;
		this.path = builder.path;
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
	 * Gets the error.
	 *
	 * @return the error
	 */
	public String getError() {
		return error;
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
	 * The Class ApiErrorBuilder.
	 */
	public static class ApiErrorBuilder {
		
		/** The status. */
		private final int status;
		
		/** The error. */
		private String error;
		
		/** The message. */
		private String message;
		
		/** The path. */
		private String path;

		/**
		 * Instantiates a new api error builder.
		 *
		 * @param status the status
		 */
		public ApiErrorBuilder(int status) {
			this.status = status;
		}

		/**
		 * Error.
		 *
		 * @param error the error
		 * @return the api error builder
		 */
		public ApiErrorBuilder error(String error) {
			this.error = error;
			return this;
		}

		/**
		 * Phone.
		 *
		 * @param message the message
		 * @return the api error builder
		 */
		public ApiErrorBuilder message(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Address.
		 *
		 * @param path the path
		 * @return the api error builder
		 */
		public ApiErrorBuilder path(String path) {
			this.path = path;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the api error
		 */
		public ApiError build() {
			ApiError user = new ApiError(this);
			return user;
		}

	}

}
