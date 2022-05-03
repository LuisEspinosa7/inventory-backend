/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The Interface MovementDetailRepository.
 * 
 * @author Luis Espinosa
 */
@Repository
public interface MovementDetailRepository extends JpaRepository<MovementDetail, Long> {

}
