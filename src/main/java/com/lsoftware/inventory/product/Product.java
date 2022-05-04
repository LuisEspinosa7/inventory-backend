/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.product;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.lsoftware.inventory.category.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The Class Product.
 * 
 * @author Luis Espinosa
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@Column(name = "prod_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The name. */
	@Column(name = "prod_name", unique = true)
	private String name;

	/** The price. */
	@Column(name = "prod_price")
	private BigDecimal price;
	
	/** The quantity. */
	@Column(name = "prod_quantity")
	private int quantity;
	
	/** The category. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cat_id")
	private Category category;
	
	/** The status. */
	@Column(name = "prod_status")
	private int status; 

}
