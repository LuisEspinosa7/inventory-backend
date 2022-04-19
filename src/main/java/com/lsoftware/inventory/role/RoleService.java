package com.lsoftware.inventory.role;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lsoftware.inventory.shared.service.ServiceListMethods;

@Service
public class RoleService implements ServiceListMethods<Role> {
	
	private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);
	
	private RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public List<Role> list() {
		LOG.info("Method: list");
		return roleRepository.findAll();
	}

}
