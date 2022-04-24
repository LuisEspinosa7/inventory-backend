/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.status;

// TODO: Auto-generated Javadoc
/**
 * The Enum Status.
 * 
 * @author Luis Espinosa
 */
public enum Status {
	
	/** The deleted. */
	DELETED(0, "It represents that the entity was deleted"),
	
	/** The active. */
	ACTIVE(1, "It represents that the entity is still active"),
	
	/** The inactive. */
	INACTIVE(2, "It represents that the entity is inactive");

	/** The digit. */
	private int digit;
	
	/** The description. */
	private String description;
	
	
	/**
	 * Instantiates a new status.
	 *
	 * @param d the d
	 * @param desc the desc
	 */
	Status(int d, String desc){
		digit = d;
		description = desc;
	}
	
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getDigit() {
		return digit;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
}
