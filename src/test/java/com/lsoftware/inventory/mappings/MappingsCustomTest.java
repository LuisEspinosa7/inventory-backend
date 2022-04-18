/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.mappings;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsoftware.inventory.authentication.request.RequestAuthenticationData;


/**
 * The Class MappingsCustomTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class MappingsCustomTest {
		
	/** The under test. */
	private MappingsCustom underTest;
	
	/** The object mapper. */
	@Mock
	private ObjectMapper objectMapper;
	
	/** The http servlet request. */
	@Mock
	private HttpServletRequest httpServletRequest;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new MappingsCustom(objectMapper);
	}

	/**
	 * It should read value.
	 *
	 * @throws StreamReadException the stream read exception
	 * @throws DatabindException the databind exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	void itShouldReadValue() throws StreamReadException, DatabindException, IOException {
		RequestAuthenticationData result = underTest
				.readValue(httpServletRequest.getInputStream(), RequestAuthenticationData.class);
	
		assertThat(result).isNull();
	}
	
}
