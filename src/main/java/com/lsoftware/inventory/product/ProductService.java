/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import java.math.BigDecimal;
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

import com.lsoftware.inventory.category.Category;
import com.lsoftware.inventory.category.CategoryRepository;
import com.lsoftware.inventory.exception.ExceptionInternalServerError;
import com.lsoftware.inventory.exception.ExceptionObjectNotFound;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.service.ServiceListMethods;
import com.lsoftware.inventory.shared.service.ServiceMethods;
import com.lsoftware.inventory.shared.service.ServicePaginatedMethods;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class ProductService.
 * 
 * @author Luis Espinosa
 */
@Service
public class ProductService implements ServicePaginatedMethods<ProductDTO>, ServiceListMethods<ProductDTO>, ServiceMethods<ProductDTO> {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
	
	/** The product repository. */
	private ProductRepository productRepository;
	
	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/** The message source. */
	private MessageSource messageSource;
	
	/** The category repository. */
	CategoryRepository categoryRepository;
	
	/**
	 * Instantiates a new category service.
	 *
	 * @param productRepository the product repository
	 * @param modelMapper the model mapper
	 * @param messageSource the message source
	 * @param categoryRepository the category repository
	 */
	public ProductService(ProductRepository productRepository, ModelMapper modelMapper,
			MessageSource messageSource, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
		this.categoryRepository = categoryRepository;
	}

	/**
	 * Adds the.
	 *
	 * @param obj the obj
	 * @return the product DTO
	 */
	@Transactional
	@Override
	public ProductDTO add(ProductDTO obj) {
		LOG.info("method: add");
		
		Optional<Product> search = 
				productRepository.findByNameAndStatus(obj.getName().toUpperCase(), 
						List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()));
		
		if (search.isPresent()) throw new ExceptionValueNotPermitted(
					messageSource.getMessage("error.alreadyExist", new String[] {"Product"}, LocaleContextHolder.getLocale())
			);
		
		Product product = modelMapper.map(obj, Product.class);
		
		product.setName(product.getName().toUpperCase());
		product.setStatus(Status.ACTIVE.getDigit());
		product.setPrice(new BigDecimal(obj.getPrice()));
		
		Category category = categoryRepository.findAll()
			.stream()
			.filter(c -> c.getId() == obj.getCategory().getId())
			.findAny()
			.orElseThrow(() -> new ExceptionObjectNotFound(
					messageSource.getMessage("error.notFound", new String[] {"Product category "}, LocaleContextHolder.getLocale())
			));
		
		product.setCategory(category);
		
		Product saved = productRepository.save(product);
		return modelMapper.map(saved, ProductDTO.class);
	}
	
	/**
	 * Update.
	 *
	 * @param obj the obj
	 * @return the product DTO
	 */
	@Transactional
	@Override
	public ProductDTO update(ProductDTO obj) {
		LOG.info("method: update");
		
		Product product = productRepository
				.findByIdAndStatus(obj.getId(), List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()))
				.orElseThrow(() -> new ExceptionValueNotPermitted(
				messageSource.getMessage("error.notFound", new String[] {"Category"}, LocaleContextHolder.getLocale())
		));
		
		product.setName(obj.getName().toUpperCase());
		product.setStatus(obj.getStatus());
		product.setPrice(new BigDecimal(obj.getPrice()));
		
		Category category = categoryRepository.findAll()
				.stream()
				.filter(c -> c.getId() == obj.getCategory().getId())
				.findAny()
				.orElseThrow(() -> new ExceptionObjectNotFound(
						messageSource.getMessage("error.notFound", new String[] {"Product category "}, LocaleContextHolder.getLocale())
				));
			
		product.setCategory(category);
		
		Product saved = productRepository.save(product);
		return modelMapper.map(saved, ProductDTO.class);
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
			
		Optional<Product> found = productRepository.findByIdAndStatus(id, 
				List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()))
				.map(obj -> Optional.ofNullable(obj))
				.orElseThrow(() -> new ExceptionObjectNotFound(
						messageSource.getMessage("error.notFound", new String[] {"Product"}, LocaleContextHolder.getLocale())
				));
		
		int result = productRepository.setStatusById(Status.DELETED.getDigit(), found.get().getId());
		if (result < 1) throw new ExceptionInternalServerError(
				messageSource.getMessage("error.notDeleted", new String[] {"Product"}, LocaleContextHolder.getLocale()));
	}

	/**
	 * List.
	 *
	 * @return the list
	 */
	@Override
	public List<ProductDTO> list() {
		LOG.info("method: list");
		
		List<ProductDTO> list = productRepository.findByStatus(Status.ACTIVE.getDigit())
				.stream()
				.map(c -> modelMapper.map(c, ProductDTO.class))
				.collect(Collectors.toList());
		
		return list;
	}

	/**
	 * Find all.
	 *
	 * @param pageAndSort the page and sort
	 * @return the response pagination and sort DTO
	 */
	@Override
	public ResponsePaginationAndSortDTO<ProductDTO> findAll(RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: findAll");
		
		Page<Product> results = productRepository
				.findByStatus(List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()), 
						PageRequest.of(pageAndSort.getPage(), pageAndSort.getSize()));
		
		List<ProductDTO> mapped = results.getContent()
			.stream()
			.map(c -> modelMapper.map(c, ProductDTO.class))
			.collect(Collectors.toList());
		
		ResponsePaginationAndSortDTO<ProductDTO> resultData = 
				new ResponsePaginationAndSortDTO<>(mapped, results.getNumber(), 
						results.getTotalElements(), results.getTotalPages());
		
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
	public ResponsePaginationAndSortDTO<ProductDTO> findByTermContaining(String searchTerm,
			RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: findByTermContaining");
		
		Page<Product> results = productRepository
				.findByTermsContaining(
						searchTerm, 
						List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()), 
						PageRequest.of(pageAndSort.getPage(), pageAndSort.getSize()));
		
		List<ProductDTO> mapped = results.getContent()
			.stream()
			.map(c -> modelMapper.map(c, ProductDTO.class))
			.collect(Collectors.toList());
		
		ResponsePaginationAndSortDTO<ProductDTO> resultData = 
				new ResponsePaginationAndSortDTO<>(mapped, results.getNumber(), 
						results.getTotalElements(), results.getTotalPages());
		
		return resultData;
	}
	
}
