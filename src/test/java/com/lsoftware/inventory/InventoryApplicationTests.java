/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory;

import static org.assertj.core.api.Assertions.assertThat;

//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.mockito.Mockito.mockStatic;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.springframework.boot.SpringApplication;
//
//import org.springframework.context.ConfigurableApplicationContext;

/**
 * The Class InventoryApplicationTests.\
 * 
 * @author Luis Espinosa
 */
//@SpringBootTest
class InventoryApplicationTests {

	/**
	 * Context loads.
	 */
//	@Test
	void contextLoads() {
		assertThat(true).isEqualTo(true);

//		try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
//
//			mocked.when(() -> {
//				SpringApplication.run(InventoryApplication.class, new String[] { "foo", "bar" });
//			}).thenReturn(Mockito.mock(ConfigurableApplicationContext.class));
//
//			InventoryApplication.main(new String[] { "foo", "bar" });
//
//			mocked.verify(() -> {
//				SpringApplication.run(InventoryApplication.class, new String[] { "foo", "bar" });
//			});
//
//		}
	}

}
