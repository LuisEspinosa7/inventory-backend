/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.lsoftware.inventory.category.Category;
import com.lsoftware.inventory.category.CategoryRepository;
import com.lsoftware.inventory.exception.ExceptionInternalServerError;
import com.lsoftware.inventory.exception.ExceptionObjectNotFound;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class ProductServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	/** The under test. */
	private ProductService underTest;
	
	/** The product repository. */
	@Mock
	private ProductRepository productRepository;

	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/** The message source. */
	@Mock
	private MessageSource messageSource;
	
	/** The category repository. */
	@Mock
	private CategoryRepository categoryRepository;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		modelMapper = new ModelMapper();
		underTest = new ProductService(productRepository, modelMapper, messageSource, categoryRepository);
	}

	/**
	 * It should add new product.
	 */
	@Test
	void itShouldAddNewProduct() {
		BDDMockito.given(productRepository.findByNameAndStatus(anyString(), anyList()))
		.willReturn(Optional.empty());
		
		BDDMockito.given(categoryRepository.findAll())
		.willReturn(getCategoryList());
		
		BDDMockito.given(productRepository.save(any()))
			.willReturn(getProductEntitySaved());
		
		ProductDTO result = underTest.add(getProductDTO());
		assertThat(result.getName()).isEqualTo(getProductEntitySaved().getName());
		assertThat(result.getId()).isEqualTo(getProductEntitySaved().getId());
		assertThat(result.getStatus()).isEqualTo(getProductEntitySaved().getStatus());
		verify(productRepository, times(1)).save(any());
	}
	
	
	/**
	 * It should fail adding new product already exist.
	 */
	@Test
	void itShouldFailAddingNewProductAlreadyExist() {
		BDDMockito.given(productRepository.findByNameAndStatus(anyString(), anyList()))
		.willReturn(Optional.of(getProductEntitySaved()));
		
		ProductDTO dto = getProductDTO();
		
		assertThatThrownBy(() -> underTest.add(dto))
			.isInstanceOf(ExceptionValueNotPermitted.class);
	
		verify(productRepository, times(0)).save(any());
	}
	
	
	/**
	 * It should fail adding new product category does not exist.
	 */
	@Test
	void itShouldFailAddingNewProductCategoryDoesNotExist() {
		BDDMockito.given(productRepository.findByNameAndStatus(anyString(), anyList()))
			.willReturn(Optional.empty());
		
		BDDMockito.given(categoryRepository.findAll())
		.willReturn(List.of());
		
		ProductDTO dto = getProductDTO();
		assertThatThrownBy(() -> underTest.add(dto))
		.isInstanceOf(ExceptionObjectNotFound.class);
	
		verify(productRepository, times(0)).save(any());
	}
	
	
	
	/**
	 * It should update product.
	 */
	@Test
	void itShouldUpdateProduct() {
		
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getProductEntitySaved()));
		
		BDDMockito.given(categoryRepository.findAll())
			.willReturn(getCategoryList());
		
		BDDMockito.given(productRepository.save(any()))
		.willReturn(getProductEntitySaved());
		
		ProductDTO result = underTest.update(getProductDTO());
		assertThat(result.getName()).isEqualTo(getProductEntitySaved().getName());
		assertThat(result.getId()).isEqualTo(getProductEntitySaved().getId());
		assertThat(result.getStatus()).isEqualTo(getProductEntitySaved().getStatus());
		verify(productRepository, times(1)).save(any());
	}
	
	
	/**
	 * It should fail updating product does not exist.
	 */
	@Test
	void itShouldFailUpdatingProductDoesNotExist() {
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
		.willReturn(Optional.empty());
		
		ProductDTO dto = getProductDTO();
		
		assertThatThrownBy(() -> underTest.update(dto))
			.isInstanceOf(ExceptionValueNotPermitted.class);
	
		verify(productRepository, times(0)).save(any());
	}
	
	
	
	/**
	 * It should fail updating product category does not exist.
	 */
	@Test
	void itShouldFailUpdatingProductCategoryDoesNotExist() {
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getProductEntitySaved()));
		
		BDDMockito.given(categoryRepository.findAll())
			.willReturn(getCategoryList());
		
		ProductDTO dto = getProductDTODifferentCategory();
		
		assertThatThrownBy(() -> underTest.update(dto))
		.isInstanceOf(ExceptionObjectNotFound.class);
	
		verify(productRepository, times(0)).save(any());
	}
	
	
	/**
	 * It should delete product.
	 */
	@Test
	void itShouldDeleteProduct() {
		
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getProductEntitySaved()));

		BDDMockito.given(productRepository.setStatusById(anyInt(), anyLong()))
		.willReturn(1);
		
		underTest.delete(1L);
		verify(productRepository, times(1)).setStatusById(anyInt(), anyLong());
	}
	
	
	/**
	 * It should fail deleting product does not exists.
	 */
	@Test
	void itShouldFailDeletingProductDoesNotExists() {
		
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.empty());

		assertThatThrownBy(() -> underTest.delete(1L))
			.isInstanceOf(ExceptionObjectNotFound.class);
		
		verify(productRepository, times(0)).setStatusById(anyInt(), anyLong());
	}
	
	
	/**
	 * It should fail deleting user status not modified.
	 */
	@Test
	void itShouldFailDeletingUserStatusNotModified() {
	
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getProductEntitySaved()));

		BDDMockito.given(productRepository.setStatusById(anyInt(), anyLong()))
			.willReturn(0);
		
		assertThatThrownBy(() -> underTest.delete(1L))
			.isInstanceOf(ExceptionInternalServerError.class);
		
		verify(productRepository, times(1)).setStatusById(anyInt(), anyLong());
	}
	
	
	
	/**
	 * It should list products by status and pageable list all available products.
	 */
	@Test
	void itShouldListProductsByStatusAndPageableListAllAvailableProducts(){
		
		BDDMockito.given(productRepository.findByStatus(anyInt()))
			.willReturn(
					List.of(
					getProductEntitySaved(),
					new Product(2L, "PRODUCT 2", new BigDecimal("12000"), 2, new Category(1L, "CATEGORY 1", 1), Status.ACTIVE.getDigit())
			));
		
		
		List<ProductDTO> results = underTest.list();
		assertEquals(2, results.size());
	}
	
	
	
	/**
	 * It should list productss by status and pageable find all.
	 */
	@Test
	void itShouldListProductssByStatusAndPageableFindAll(){
		
		Page<Product> page = new PageImpl<>(
				List.of(
						getProductEntitySaved(),
						new Product(2L, "PRODUCT 2", new BigDecimal("12000"), 2, new Category(1L, "CATEGORY 1", 1), Status.ACTIVE.getDigit())
				)
		);
		
		BDDMockito.given(productRepository.findByStatus(anyList(), any()))
			.willReturn(page);
		
		RequestPaginationAndSortDTO request = new RequestPaginationAndSortDTO();
		request.setPage(0);
		request.setSize(2);
		ResponsePaginationAndSortDTO<ProductDTO> results = underTest.findAll(request);
		assertEquals(2, results.getResult().size());
	}
	
	/**
	 * It should list products by status and pageable and search term.
	 */
	@Test
	void itShouldListProductsByStatusAndPageableAndSearchTerm(){
		
		Page<Product> page = new PageImpl<>(
				List.of(
						new Product(2L, "PRODUCT 2", new BigDecimal("12000"), 2, new Category(1L, "CATEGORY 1", 1), Status.ACTIVE.getDigit())
				)
		);
		
		BDDMockito.given(productRepository.findByTermsContaining(anyString(), anyList(), any()))
			.willReturn(page);
		
		RequestPaginationAndSortDTO request = new RequestPaginationAndSortDTO();
		request.setPage(0);
		request.setSize(2);
		ResponsePaginationAndSortDTO<ProductDTO> results = underTest.findByTermContaining("Val", request);
		assertEquals(1, results.getResult().size());
	}
	
	
	
	/**
	 * Gets the product DTO.
	 *
	 * @return the product DTO
	 */
	private ProductDTO getProductDTO() {
		return new ProductDTO(1L, "Product 1", "12000", 2, new Category(1L, "CATEGORY 1", 1), Status.ACTIVE.getDigit());
	}
	
	/**
	 * Gets the product DTO different category.
	 *
	 * @return the product DTO different category
	 */
	private ProductDTO getProductDTODifferentCategory() {
		return new ProductDTO(1L, "Product 1", "12000", 2, new Category(3L, "CATEGORY 3", 1), Status.ACTIVE.getDigit());
	}
	
	/**
	 * Gets the product entity saved.
	 *
	 * @return the product entity saved
	 */
	private Product getProductEntitySaved() {
		return new Product(1L, "PRODUCT 1", new BigDecimal("12000"), 2, new Category(1L, "CATEGORY 1", 1), Status.ACTIVE.getDigit());
	}
	
	
	/**
	 * Gets the category list.
	 *
	 * @return the category list
	 */
	private List<Category> getCategoryList() {
		return List.of(
				new Category(1L, "CATEGORY 1", 1),
				new Category(2L, "CATEGORY 2", 1)
		);
	}
	
	
	
}
