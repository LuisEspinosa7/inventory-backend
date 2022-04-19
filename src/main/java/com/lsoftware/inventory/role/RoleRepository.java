/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface RoleRepository.
 * 
 * @author Luis Espinosa
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
}
