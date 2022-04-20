/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.role;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lsoftware.inventory.shared.service.ServiceListMethods;

/**
 * The Class RoleService.
 * 
 * @author Luis Espinosa
 */
@Service
public class RoleService implements ServiceListMethods<RoleDTO> {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);
	
	/** The role repository. */
	private RoleRepository roleRepository;
	
	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/**
	 * Instantiates a new role service.
	 *
	 * @param roleRepository the role repository
	 * @param modelMapper the model mapper
	 */
	public RoleService(RoleRepository roleRepository, ModelMapper modelMapper) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	/**
	 * List.
	 *
	 * @return the list
	 */
	@Override
	public List<RoleDTO> list() {
		LOG.info("Method: list");
		
		List<RoleDTO> roles = roleRepository.findAll()
				.stream()
				.map(r -> modelMapper.map(r, RoleDTO.class))
				.collect(Collectors.toList());
		
		return roles;
	}

}
