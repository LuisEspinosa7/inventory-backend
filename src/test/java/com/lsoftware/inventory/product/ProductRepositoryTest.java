package com.lsoftware.inventory.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.lsoftware.inventory.category.Category;
import com.lsoftware.inventory.category.CategoryRepository;
import com.lsoftware.inventory.shared.status.Status;

@DataJpaTest
class ProductRepositoryTest {

	/** The under test. */
	@Autowired
	private ProductRepository underTest;
	
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		underTest.deleteAll();
	}
	
	
	@Test
	void itShouldFindByNameAndStatus() {
		
		Category cat = new Category();
		cat.setName("CATEGORY1");
		cat.setStatus(Status.ACTIVE.getDigit());
		categoryRepository.save(cat);
		
		Product prod = new Product();
		prod.setName("CATEGORY1");
		prod.setPrice(new BigDecimal("12000"));
		prod.setQuantity(2);
		prod.setCategory(cat);
		prod.setStatus(Status.ACTIVE.getDigit());
		underTest.save(prod);
		
		Optional<Product> result = underTest.findByNameAndStatus(prod.getName(), List.of(Status.ACTIVE.getDigit()));;
		assertEquals(true, result.isPresent());
		assertThat(result.get().getName()).isEqualTo(prod.getName());
		assertThat(result.get().getStatus()).isEqualTo(prod.getStatus());
	}

}
