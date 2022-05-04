/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The Class MovementTypeServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class MovementTypeServiceTest {

	/** The under test. */
	private MovementTypeService underTest;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new MovementTypeService();
	}

	/**
	 * It should list movement types.
	 */
	@Test
	void itShouldListMovementTypes() {
		List<MovementTypeDTO> list = underTest.list();
		assertEquals(2, list.size());
	}

}
