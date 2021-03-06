/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsoftware.inventory.exception.ExceptionObjectNotFound;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.product.ProductRepository;
import com.lsoftware.inventory.exception.ExceptionInternalServerError;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.service.ServiceListMethods;
import com.lsoftware.inventory.shared.service.ServiceMethods;
import com.lsoftware.inventory.shared.service.ServicePaginatedMethods;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class CategoryService.
 * 
 * @author Luis Espinosa
 */
@Service
public class CategoryService implements ServicePaginatedMethods<CategoryDTO>, ServiceListMethods<CategoryDTO>, ServiceMethods<CategoryDTO> {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
	
	/** The Constant ERROR_NOT_FOUND_NAME. */
	private static final String ERROR_NOT_FOUND_NAME = "error.notFound";
	
	/** The Constant CATEGORY_TEXT. */
	private static final String CATEGORY_TEXT = "Category";
	
	/** The category repository. */
	private CategoryRepository categoryRepository;
	
	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/** The message source. */
	private MessageSource messageSource;
	
	/** The product repository. */
	private ProductRepository productRepository;
	
	/**
	 * Instantiates a new category service.
	 *
	 * @param categoryRepository the category repository
	 * @param modelMapper the model mapper
	 * @param messageSource the message source
	 */
	public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper,
			MessageSource messageSource,
			ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
		this.productRepository = productRepository;
	}

	/**
	 * Adds the.
	 *
	 * @param obj the obj
	 * @return the category DTO
	 */
	@Transactional
	@Override
	public CategoryDTO add(CategoryDTO obj) {
		LOG.info("method: add");
		
		Optional<Category> search = 
				categoryRepository.findByNameAndStatus(obj.getName().toUpperCase(), 
						Status.ACTIVE.getDigit());
		
		if (search.isPresent()) throw new ExceptionValueNotPermitted(
					messageSource.getMessage("error.alreadyExist", new String[] {CATEGORY_TEXT}, LocaleContextHolder.getLocale())
			);
		
		Category category = modelMapper.map(obj, Category.class);
		
		category.setName(category.getName().toUpperCase());
		category.setStatus(Status.ACTIVE.getDigit());
		
		Category saved = categoryRepository.save(category);
		return modelMapper.map(saved, CategoryDTO.class);
	}

	/**
	 * Update.
	 *
	 * @param obj the obj
	 * @return the category DTO
	 */
	@Transactional
	@Override
	public CategoryDTO update(CategoryDTO obj) {
		LOG.info("method: update");
		
		Optional<Category> categoryById = categoryRepository.findByIdAndStatus(obj.getId(), Status.ACTIVE.getDigit());
		
		if (categoryById.isEmpty()) throw new ExceptionValueNotPermitted(
				messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {CATEGORY_TEXT}, LocaleContextHolder.getLocale())
		);
		
		Category foundObj = categoryById.get();
		foundObj.setName(obj.getName().toUpperCase());
		
		Category saved = categoryRepository.save(foundObj);
		return modelMapper.map(saved, CategoryDTO.class);
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@Transactional
	@Override
	public void delete(Long id) {
		LOG.info("method: delete");
		
		long count = productRepository.countByCategoryId(id);
		
		if (count > 0) throw new ExceptionValueNotPermitted(
				messageSource.getMessage("error.isBeingUsed", new String[] {CATEGORY_TEXT, "Product"}, LocaleContextHolder.getLocale()));
		
		Optional<Category> category = categoryRepository.findById(id)
				.map(Optional::ofNullable)
				.orElseThrow(() -> new ExceptionObjectNotFound(
						messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {CATEGORY_TEXT}, LocaleContextHolder.getLocale())
				));
		
		if (category.isEmpty()) {
			throw new ExceptionObjectNotFound(
					messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {CATEGORY_TEXT}, LocaleContextHolder.getLocale())
			);
		}
		
		int result = categoryRepository.setStatusById(Status.DELETED.getDigit(), category.get().getId());
		if (result < 1) throw new ExceptionInternalServerError(
				messageSource.getMessage("error.notDeleted", new String[] {CATEGORY_TEXT}, LocaleContextHolder.getLocale()));
	}

	/**
	 * List.
	 *
	 * @return the list
	 */
	@Override
	public List<CategoryDTO> list() {
		LOG.info("method: list");
		
		return categoryRepository.findByStatus(Status.ACTIVE.getDigit())
				.stream()
				.map(c -> modelMapper.map(c, CategoryDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * Find all.
	 *
	 * @param pageAndSort the page and sort
	 * @return the response pagination and sort DTO
	 */
	@Override
	public ResponsePaginationAndSortDTO<CategoryDTO> findAll(RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: findAll");
		
		Page<Category> results = categoryRepository
				.findByStatus(Status.ACTIVE.getDigit(), PageRequest.of(pageAndSort.getPage(), pageAndSort.getSize()));
		
		List<CategoryDTO> mapped = results.getContent()
			.stream()
			.map(c -> modelMapper.map(c, CategoryDTO.class))
			.collect(Collectors.toList());
		
		ResponsePaginationAndSortDTO<CategoryDTO> resultData = new ResponsePaginationAndSortDTO<>();
		resultData.setResult(mapped);
		resultData.setCurrentPage(results.getNumber());
		resultData.setTotalItems(results.getTotalElements());
		resultData.setTotalPages(results.getTotalPages());
		return resultData;
	}

	/**
	 * Find by term containing.
	 *
	 * @param searchTerm the search term
	 * @param pageAndSort the page and sort
	 * @return the response pagination and sort DTO
	 */
	@Override
	public ResponsePaginationAndSortDTO<CategoryDTO> findByTermContaining(String searchTerm,
			RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: findByTermContaining");
		
		Page<Category> results = categoryRepository
				.findByTermsContaining(
						searchTerm, 
						List.of(Status.ACTIVE.getDigit()), 
						PageRequest.of(pageAndSort.getPage(), pageAndSort.getSize()));
		
		List<CategoryDTO> mapped = results.getContent()
			.stream()
			.map(c -> modelMapper.map(c, CategoryDTO.class))
			.collect(Collectors.toList());
		
		return new ResponsePaginationAndSortDTO<>(mapped, results.getNumber(), 
						results.getTotalElements(), results.getTotalPages());
	}
	
}
