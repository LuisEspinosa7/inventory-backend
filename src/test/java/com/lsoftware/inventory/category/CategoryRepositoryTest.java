/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class CategoryRepositoryTest.
 * 
 * @author Luis Espinosa
 */
@DataJpaTest
class CategoryRepositoryTest {
	
	/** The under test. */
	@Autowired
	private CategoryRepository underTest;

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		underTest.deleteAll();
	}

	/**
	 * It should find by name and status.
	 */
	@Test
	void itShouldFindByNameAndStatus() {
		Category cat = new Category();
		cat.setName("CATEGORY1");
		cat.setStatus(Status.ACTIVE.getDigit());
		underTest.save(cat);
		
		Optional<Category> result = underTest.findByNameAndStatus(cat.getName(), cat.getStatus());
		assertEquals(true, result.isPresent());
		assertThat(result.get().getName()).isEqualTo(cat.getName());
		assertThat(result.get().getStatus()).isEqualTo(cat.getStatus());
	}
	
	/**
	 * It should find by id and status then set status by id.
	 */
	@Test
	void itShouldFindByIdAndStatusThenSetStatusById() {
		Category cat = new Category();
		cat.setName("CATEGORY1");
		cat.setStatus(Status.ACTIVE.getDigit());
		Category saved = underTest.save(cat);
		
		Optional<Category> result = underTest.findByIdAndStatus(saved.getId(), cat.getStatus());
		assertEquals(true, result.isPresent());
		assertThat(result.get().getName()).isEqualTo(cat.getName());
		assertThat(result.get().getStatus()).isEqualTo(cat.getStatus());
		
		int modified = underTest.setStatusById(Status.DELETED.getDigit(), saved.getId());
		assertThat(modified).isEqualTo(1);
	}
	
	
	/**
	 * It should find by terms containing.
	 */
	@Test
	void itShouldFindByTermsContaining() {
		Category cat = new Category();
		cat.setName("CATEGORY1");
		cat.setStatus(Status.ACTIVE.getDigit());
		underTest.save(cat);
		
		PageRequest pageable = PageRequest.of(0, 2);
		
		Page<Category> result = underTest.findByTermsContaining("C", List.of(Status.ACTIVE.getDigit()), pageable);
		assertEquals(false, result.getContent().isEmpty());
		assertEquals(1, result.getContent().size());
	}
	
	/**
	 * It should find by status.
	 */
	@Test
	void itShouldFindByStatus() {
		
		underTest.save(new Category(1L, "CATEGORY1", Status.ACTIVE.getDigit()));
		underTest.save(new Category(2L, "CATEGORY2", Status.ACTIVE.getDigit()));
		
		List<Category> results = underTest.findByStatus(Status.ACTIVE.getDigit());
		assertEquals(2, results.size());
	}
	
	/**
	 * It should find by status pageable.
	 */
	@Test
	void itShouldFindByStatusPageable() {
		
		underTest.save(new Category(1L, "CATEGORY1", Status.ACTIVE.getDigit()));
		underTest.save(new Category(2L, "CATEGORY2", Status.ACTIVE.getDigit()));
		underTest.save(new Category(3L, "CATEGORY3", Status.ACTIVE.getDigit()));
		underTest.save(new Category(4L, "CATEGORY4", Status.ACTIVE.getDigit()));
		underTest.save(new Category(5L, "CATEGORY5", Status.DELETED.getDigit()));
		
		Page<Category> results = underTest.findByStatus(Status.ACTIVE.getDigit(),
				PageRequest.of(0, 2));
		assertThat(results.get().count()).isEqualTo(2);
	}


}
