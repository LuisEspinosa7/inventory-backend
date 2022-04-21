/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(result.isPresent()).isTrue();
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
		underTest.save(cat);
		
		Optional<Category> result = underTest.findByIdAndStatus(1L, cat.getStatus());
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getName()).isEqualTo(cat.getName());
		assertThat(result.get().getStatus()).isEqualTo(cat.getStatus());
		
		int modified = underTest.setStatusById(Status.DELETED.getDigit(), 1L);
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
		assertThat(result.getContent().isEmpty()).isFalse();
		assertThat(result.getContent().size()).isEqualTo(1);
	}

}
