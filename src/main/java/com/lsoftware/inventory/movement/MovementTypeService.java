/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lsoftware.inventory.shared.service.ServiceListMethods;

/**
 * The Class MovementTypeService.
 * 
 * @author Luis Espinosa
 */
@Service
public class MovementTypeService implements ServiceListMethods<MovementTypeDTO> {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(MovementTypeService.class);
	
	/**
	 * List.
	 *
	 * @return the list
	 */
	@Override
	public List<MovementTypeDTO> list() {
		LOG.info("method: list");
		
		return Arrays.asList(MovementType.values())
			.stream()
			.map(m ->
				new MovementTypeDTO(m.getName(), m.getDescription()))
			.collect(Collectors.toList());
	}
	
}
