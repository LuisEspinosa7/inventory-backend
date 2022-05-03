/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lsoftware.inventory.product.Product;

import lombok.Data;


/**
 * The Class MovementDetail.
 * 
 * @author Luis Espinosa
 */
@Data
@Entity
@Table(name = "movement_details")
public class MovementDetail implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@Column(name = "mod_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The movement. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mov_id")
	@JsonIgnore
	private Movement movement;
	
	/** The product. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_id")
	private Product product;
	
	/** The quantity. */
	@Column(name = "mod_quantity")
	private int quantity;

}
