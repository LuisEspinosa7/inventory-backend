/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.lsoftware.inventory.exception.ExceptionInternalServerError;
import com.lsoftware.inventory.exception.ExceptionObjectNotFound;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.product.ProductRepository;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class CategoryServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	
	/** The under test. */
	private CategoryService underTest;
	
	/** The category repository. */
	@Mock
	private CategoryRepository categoryRepository;

	/** The model mapper. */
	@Mock
	private ModelMapper modelMapper;
	
	/** The message source. */
	@Mock
	private MessageSource messageSource;
	
	@Mock
	private ProductRepository productRepository;
	
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new CategoryService(categoryRepository, modelMapper, messageSource, productRepository);
	}

	/**
	 * It should add new category.
	 */
	@Test
	void itShouldAddNewCategory() {
		String name = "Category1";
		
		CategoryDTO dto = new CategoryDTO();
		dto.setName(name);
		
		Category category = new Category();
		category.setName(name);
		
		BDDMockito.given(categoryRepository.findByNameAndStatus(anyString(), anyInt()))
			.willReturn(Optional.empty());
		
		BDDMockito.given(modelMapper.map(any(), eq(Category.class)))
			.willReturn(category);
		
		category.setId(1L);
		category.setStatus(Status.ACTIVE.getDigit());
		BDDMockito.given(categoryRepository.save(any()))
			.willReturn(category);
		
		dto.setId(1L);
		dto.setStatus(Status.ACTIVE.getDigit());
		dto.setName("CATEGORY1");
		BDDMockito.given(modelMapper.map(any(), eq(CategoryDTO.class)))
			.willReturn(dto);
		
		CategoryDTO result = underTest.add(dto);
		assertThat(result.getName()).isEqualTo(dto.getName().toUpperCase());
		assertThat(result.getId()).isEqualTo(dto.getId());
		assertThat(result.getStatus()).isEqualTo(dto.getStatus());
		verify(categoryRepository, times(1)).save(any());
	}
	
	/**
	 * It should fail creating category already exist.
	 */
	@Test
	void itShouldFailCreatingCategoryAlreadyExist() {
		Category category = new Category();
		category.setName("CATEGORY1");
		category.setId(1L);
		category.setStatus(Status.ACTIVE.getDigit());
		
		CategoryDTO dto = new CategoryDTO();
		dto.setName("CATEGORY1");
		
		BDDMockito.given(categoryRepository.findByNameAndStatus(Mockito.anyString(), anyInt()))
			.willReturn(Optional.of(category));
		
		assertThatThrownBy(() -> underTest.add(dto))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		
		verify(categoryRepository, times(0)).save(any());
	}
	
	
	
	/**
	 * It should update category.
	 */
	@Test
	void itShouldUpdateCategory() {
		String name = "CATEGORY1";
		
		Category category = new Category();
		category.setId(1L);
		category.setName(name);
		category.setStatus(Status.ACTIVE.getDigit());
		
		CategoryDTO dto = new CategoryDTO();
		dto.setId(1L);
		dto.setName("CATEGORY1");
		dto.setStatus(Status.ACTIVE.getDigit());
		
		CategoryDTO returned = new CategoryDTO();
		dto.setId(1L);
		dto.setName("CATEGORY1MOD");
		dto.setStatus(Status.ACTIVE.getDigit());
		
		BDDMockito.given(categoryRepository.findByIdAndStatus(anyLong(), anyInt()))
			.willReturn(Optional.of(category));
		
		category.setName("CATEGORY1MOD");
		BDDMockito.given(categoryRepository.save(any()))
			.willReturn(category);

		BDDMockito.given(modelMapper.map(any(), eq(CategoryDTO.class)))
			.willReturn(returned);
		
		CategoryDTO result = underTest.update(dto);
		assertThat(result.getName()).isEqualTo(returned.getName());
		assertThat(result.getId()).isEqualTo(returned.getId());
		assertThat(result.getStatus()).isEqualTo(returned.getStatus());
		verify(categoryRepository, times(1)).save(any());
	}
	
	
	/**
	 * It should fail updating category does not exist.
	 */
	@Test
	void itShouldFailUpdatingCategoryDoesNotExist() {
		
		CategoryDTO dto = new CategoryDTO();
		dto.setId(1L);
		dto.setName("CATEGORY1M0D");
		
		BDDMockito.given(categoryRepository.findByIdAndStatus(anyLong(), anyInt()))
			.willReturn(Optional.empty());
		
		assertThatThrownBy(() -> underTest.update(dto))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		
		verify(categoryRepository, times(0)).save(any());
	}
	
	
	
	/**
	 * It should delete category.
	 */
	@Test
	void itShouldDeleteCategory() {
		
		Category category = new Category();
		category.setId(1L);
		category.setName("CATEGORY1");
		category.setStatus(Status.ACTIVE.getDigit());
		
		BDDMockito.given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));

		BDDMockito.given(categoryRepository.setStatusById(anyInt(), anyLong()))
		.willReturn(1);
		
		underTest.delete(1L);
		verify(categoryRepository, times(1)).setStatusById(anyInt(), anyLong());
	}
	
	/**
	 * It should fail deleting category status not modified.
	 */
	@Test
	void itShouldFailDeletingCategoryStatusNotModified() {
		
		Category category = new Category();
		category.setId(1L);
		category.setName("CATEGORY1");
		category.setStatus(Status.ACTIVE.getDigit());
		
		BDDMockito.given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));

		BDDMockito.given(categoryRepository.setStatusById(anyInt(), anyLong()))
		.willReturn(0);
		
		assertThatThrownBy(() -> underTest.delete(1L))
			.isInstanceOf(ExceptionInternalServerError.class);
		
		verify(categoryRepository, times(1)).setStatusById(anyInt(), anyLong());
	}
	
	/**
	 * It should fail deleting category category not found.
	 */
	@Test
	void itShouldFailDeletingCategoryCategoryNotFound() {
		
		BDDMockito.given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());
		
		assertThatThrownBy(() -> underTest.delete(1L))
			.isInstanceOf(ExceptionObjectNotFound.class);
		
		verify(categoryRepository, times(0)).setStatusById(anyInt(), anyLong());
	}
	
	/**
	 * It should list available categories.
	 */
	@Test
	void itShouldListAvailableCategories(){
		List<Category> categories = List.of(
				new Category(1L, "CATEGORY1", Status.ACTIVE.getDigit()),
				new Category(2L, "CATEGORY2", Status.ACTIVE.getDigit())
		);
		
		BDDMockito.given(categoryRepository.findByStatus(anyInt()))
			.willReturn(categories);
		
		List<CategoryDTO> results = underTest.list();
		assertEquals(2, results.size());
	}
	
	
	/**
	 * It should list categories by status and pageable.
	 */
	@Test
	void itShouldListCategoriesByStatusAndPageable(){
		
		CategoryDTO dto = new CategoryDTO(1L, "CATEGORY1", Status.ACTIVE.getDigit());
		
		Page<Category> page = new PageImpl<>(
				List.of(
						new Category(1L, "CATEGORY1", Status.ACTIVE.getDigit())
				)
		);
		
		BDDMockito.given(categoryRepository.findByStatus(anyInt(), any()))
			.willReturn(page);
		
		BDDMockito.given(modelMapper.map(any(), eq(CategoryDTO.class)))
		.willReturn(dto);
		
		RequestPaginationAndSortDTO request = new RequestPaginationAndSortDTO();
		request.setPage(0);
		request.setSize(2);
		ResponsePaginationAndSortDTO<CategoryDTO> results = underTest.findAll(request);
		assertEquals(1, results.getResult().size());
	}
	
	
	/**
	 * It should list categories by status and pageable and search term.
	 */
	@Test
	void itShouldListCategoriesByStatusAndPageableAndSearchTerm(){
		
		CategoryDTO dto = new CategoryDTO(1L, "CATEGORY1", Status.ACTIVE.getDigit());
		
		Page<Category> page = new PageImpl<>(
				List.of(
						new Category(1L, "CATEGORY1", Status.ACTIVE.getDigit())
				)
		);
		
		BDDMockito.given(categoryRepository.findByTermsContaining(anyString(), anyList(), any()))
			.willReturn(page);
		
		BDDMockito.given(modelMapper.map(any(), eq(CategoryDTO.class)))
		.willReturn(dto);
		
		RequestPaginationAndSortDTO request = new RequestPaginationAndSortDTO();
		request.setPage(0);
		request.setSize(2);
		ResponsePaginationAndSortDTO<CategoryDTO> results = underTest.findByTermContaining("CATE", request);
		assertEquals(1, results.getResult().size());
	}

}
