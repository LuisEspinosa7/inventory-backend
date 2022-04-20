/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;


/**
 * The Enum MovementType.
 * 
 * @author Luis Espinosa
 */
public enum MovementType {
	
	/** The input. */
	INPUT("INPUT", "It allows to increment the quantity of a product"),
	
	/** The output. */
	OUTPUT("OUTPUT", "It allows to decrement the quantity of a product");
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/**
	 * Instantiates a new movement type.
	 *
	 * @param d the d
	 */
	MovementType(String n, String d) {
		name = n;
		description = d;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName() {
		return name;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	String getDescription() {
		return description;
	}
}
