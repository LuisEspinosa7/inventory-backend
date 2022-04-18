/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.mappings;

import java.io.IOException;

import javax.servlet.ServletInputStream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.authentication.request.RequestAuthenticationData;

/**
 * The Class MappingsCustom.
 * Since It was not possible to mock ObjectMapper correctly, a good practice
 * is never Mock what you did not wrote.
 * 
 * @author Luis Espinosa
 */
@Component
public class MappingsCustom {
	
	/** The object mapper. */
	private ObjectMapper objectMapper;

	/**
	 * Instantiates a new mappings custom.
	 *
	 * @param objectMapper the object mapper
	 */
	public MappingsCustom(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	/**
	 * Read value.
	 *
	 * @param servletInputStream the content
	 * @param target the target
	 * @return the request authentication data
	 * @throws StreamReadException the stream read exception
	 * @throws DatabindException the databind exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public RequestAuthenticationData readValue(ServletInputStream servletInputStream, Class<RequestAuthenticationData> valueType) throws StreamReadException, DatabindException, IOException {
		return objectMapper
			.readValue(servletInputStream, valueType);
	}
	
	
}
