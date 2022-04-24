/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.shared.service;

import com.lsoftware.inventory.user.UserPasswordChangeDTO;


/**
 * The Interface ServiceUserPasswordChangeMethods.
 * 
 * @author Luis Espinosa
 */
public interface ServiceUserPasswordChangeMethods {
	
	/**
	 * Update password.
	 *
	 * @param dto the dto
	 */
	void updatePassword(UserPasswordChangeDTO dto);

}
