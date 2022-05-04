/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * The Class InventoryApplicationTests.
 * 
 * @author Luis Espinosa
 */
@SpringBootTest
class InventoryApplicationTests {

	/**
	 * Context loads.
	 */
	@Test
	void contextLoads() {
		boolean option = true;
		assertThat(option).isTrue();
	}

}
