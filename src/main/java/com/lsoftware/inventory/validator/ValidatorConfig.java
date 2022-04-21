/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * The Class ValidatorConfig.
 * 
 * @author Luis Espinosa
 */
@Configuration
public class ValidatorConfig {

	/** The message source. */
	@Autowired
	MessageSource messageSource;

	/**
	 * Gets the validator.
	 *
	 * @return the validator
	 */
	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
		validatorFactory.setValidationMessageSource(messageSource);
		return validatorFactory;
	}

}
