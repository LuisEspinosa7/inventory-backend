/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.product.Product;
import com.lsoftware.inventory.product.ProductRepository;
import com.lsoftware.inventory.shared.service.ServiceMovementMethods;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class MovementService.
 * 
 * @author Luis Espinosa
 */
@Service
public class MovementService implements ServiceMovementMethods<MovementDTO> {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(MovementService.class);

	/** The model mapper. */
	private ModelMapper modelMapper;

	/** The message source. */
	private MessageSource messageSource;

	/** The movement repository. */
	private MovementRepository movementRepository;

	/** The product repository. */
	private ProductRepository productRepository;

	/**
	 * Instantiates a new movement service.
	 *
	 * @param productRepository  the product repository
	 * @param modelMapper        the model mapper
	 * @param messageSource      the message source
	 */
	public MovementService(ProductRepository productRepository, MovementRepository movementRepository,
			ModelMapper modelMapper, MessageSource messageSource) {
		this.productRepository = productRepository;
		this.movementRepository = movementRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
	}

	/**
	 * Adds the.
	 *
	 * @param movDTO the mov DTO
	 * @return the movement
	 */
	@Transactional
	@Override
	public MovementDTO add(MovementDTO movDTO) {
		LOG.info("method: add");

		Movement movement = modelMapper.map(movDTO, Movement.class);

		for (MovementDetail movementDetail : movement.getDetails()) {
			LOG.debug("proccess: processing the details");

			movementDetail.setMovement(movement);

			Product product = productRepository
					.findByIdAndStatus(movementDetail.getProduct().getId(), List.of(Status.ACTIVE.getDigit()))
					.orElseThrow(() -> new ExceptionValueNotPermitted(messageSource.getMessage("error.notFound",
							new String[] { "Product" }, LocaleContextHolder.getLocale())));

			int updatedStock = 0;

			if (movDTO.getType().equals(MovementType.INPUT.getName())) {
				LOG.debug("proccess: increasing quantity of product= [ {} - {}", movementDetail.getProduct().getId(), movementDetail.getProduct().getName());

				updatedStock = product.getQuantity() + movementDetail.getQuantity(); // Increase

			} else if (movDTO.getType().equals(MovementType.OUTPUT.getName())) {
				LOG.debug("proccess: decreasing quantity of product= [ {} - {}", movementDetail.getProduct().getId(), movementDetail.getProduct().getName());

				updatedStock = product.getQuantity() - movementDetail.getQuantity(); // Decrement
			}
			
			if (updatedStock < 0) throw new ExceptionValueNotPermitted(
					messageSource.getMessage("error.outputExceeds", new String[] {"Product Output"}, LocaleContextHolder.getLocale())
			);

			productRepository.updateStock(updatedStock, movementDetail.getProduct().getId());
		}

		movement.setCode(UUID.randomUUID().toString());
		Movement saved = movementRepository.save(movement);
		return modelMapper.map(saved, MovementDTO.class);
	}

}
