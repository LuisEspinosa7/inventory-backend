/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.service;


/**
 * The Interface ServiceMethods.
 * 
 * @author Luis Espinosa
 *
 * @param <T> the generic type
 */
public interface ServiceMethods<T> {
	
	/**
	 * Adds the.
	 *
	 * @param obj the obj
	 * @return the t
	 */
	T add(T obj);
	
	/**
	 * Update.
	 *
	 * @param obj the obj
	 * @return the t
	 */
	T update(T obj);
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	void delete(Long id);
}
