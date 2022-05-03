/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import com.lsoftware.inventory.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * The Class Movement.
 * 
 * @author Luis Espinosa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movements")
public class Movement implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@Column(name = "mov_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The timestamp. */
	@Column(name = "mov_timestamp", updatable = false)
	@CreationTimestamp
	private LocalDateTime timestamp;	
	
	/** The type. */
	@Column(name = "mov_type")
	private String type;
	
	/** The user. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "use_id")
	private User user;

	/** The code. */
	@Column(name = "mov_code")
	private String code;
	
	/** The details. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "movement", orphanRemoval = true)
	private List<MovementDetail> details;
	
}
