/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


/**
 * Instantiates a new usuario.
 * 
 * @author Luis Espinosa
 */
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@Column(name = "use_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The document. */
	@NotBlank
	@Column(name = "use_document")
	private String document;

	/** The name. */
	@NotBlank
	@Column(name = "use_name")
	private String name;

	/** The last name. */
	@NotBlank
	@Column(name = "use_lastname")
	private String lastName;

	/** The username. */
	@NotBlank
	@Column(name = "use_username")
	private String username;

	/** The password. */
	@JsonIgnore
	@NotBlank
	@Column(name = "use_password")
	private String password;

	/** The roles. */
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "permissions", joinColumns = { @JoinColumn(name = "use_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Rol> roles = new HashSet<Rol>();

}
