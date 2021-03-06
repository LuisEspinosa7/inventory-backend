/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lsoftware.inventory.authentication.AuthenticationHolderProvider;
import com.lsoftware.inventory.exception.ExceptionInternalServerError;
import com.lsoftware.inventory.exception.ExceptionObjectNotFound;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.role.Role;
import com.lsoftware.inventory.role.RoleRepository;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.service.ServiceMethods;
import com.lsoftware.inventory.shared.service.ServicePaginatedMethods;
import com.lsoftware.inventory.shared.service.ServiceUserPasswordChangeMethods;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class UserService.
 * 
 * @author Luis Espinosa
 */
@Service
public class UserService implements ServicePaginatedMethods<UserDTO>, ServiceMethods<UserDTO>, ServiceUserPasswordChangeMethods {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	/** The Constant ERROR_NOT_FOUND_NAME. */
	private static final String ERROR_NOT_FOUND_NAME = "error.notFound";
	
	/** The user repository. */
	private UserRepository userRepository;
	
	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/** The message source. */
	private MessageSource messageSource;
	
	/** The password encoder. */
	private PasswordEncoder passwordEncoder;
	
	/** The authentication custom holder provider. */
	private AuthenticationHolderProvider authenticationHolderProvider;
	
	/** The role repository. */
	private RoleRepository roleRepository;
	
	
	/**
	 * Instantiates a new user service.
	 *
	 * @param userRepository the user repository
	 * @param modelMapper the model mapper
	 * @param messageSource the message source
	 * @param passwordEncoder the password encoder
	 */
	public UserService(UserRepository userRepository, ModelMapper modelMapper,
			MessageSource messageSource,
			PasswordEncoder passwordEncoder,
			AuthenticationHolderProvider authenticationHolderProvider,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.messageSource = messageSource;
		this.passwordEncoder = passwordEncoder;
		this.authenticationHolderProvider = authenticationHolderProvider;
		this.roleRepository = roleRepository;
	}
	
	/**
	 * Adds the.
	 *
	 * @param obj the obj
	 * @return the user DTO
	 */
	@Transactional
	@Override
	public UserDTO add(UserDTO obj) {
		LOG.info("method: add");
		
		Optional<User> search = 
				userRepository.findByDocumentAndUsernameAndStatus(obj.getDocument(), obj.getUsername().toUpperCase(), 
						List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()));
		
		if (search.isPresent()) throw new ExceptionValueNotPermitted(
					messageSource.getMessage("error.alreadyExist", new String[] {"User"}, LocaleContextHolder.getLocale())
			);
		
		User user = modelMapper.map(obj, User.class);
		
		user.setUsername(obj.getUsername().toUpperCase());
		user.setPassword(passwordEncoder.encode(obj.getPassword()));
		user.setStatus(Status.ACTIVE.getDigit());
		
		Set<Role> foundRoles = roleRepository.findAll().stream().collect(Collectors.toSet());
		Set<Role> filtered1 = new HashSet<>();
		
		filterRoles(foundRoles, filtered1, obj);
		
		if (filtered1.isEmpty()) throw new ExceptionValueNotPermitted(
				messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {"User roles "}, LocaleContextHolder.getLocale())
		);
		
		user.setRoles(filtered1);
		
		User saved = userRepository.save(user);
		saved.setPassword("000");
		return modelMapper.map(saved, UserDTO.class);
	}

	/**
	 * Update.
	 *
	 * @param obj the obj
	 * @return the user DTO
	 */
	@Transactional
	@Override
	public UserDTO update(UserDTO obj) {
		LOG.info("method: update");
		
		Optional<User> categoryById = userRepository.findByIdAndStatus(obj.getId(), 
				List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()));
		
		if (categoryById.isEmpty()) throw new ExceptionValueNotPermitted(
				messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {"User"}, LocaleContextHolder.getLocale())
		);
		
		User foundObj = categoryById.get();
		foundObj.setName(obj.getName());
		foundObj.setLastName(obj.getLastName());
		foundObj.setStatus(obj.getStatus());
		
		Set<Role> foundRoles = roleRepository.findAll().stream().collect(Collectors.toSet());
		Set<Role> filtered = new HashSet<>();
		
		filterRoles(foundRoles, filtered, obj);
		
		if (filtered.isEmpty()) throw new ExceptionValueNotPermitted(
				messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {"User roles "}, LocaleContextHolder.getLocale())
		);
		
		foundObj.setRoles(filtered);
		
		User saved = userRepository.save(foundObj);
		saved.setPassword("000");
		return modelMapper.map(saved, UserDTO.class);
	}
	
	
	/**
	 * Filter roles.
	 *
	 * @param foundRoles the found roles
	 * @param filtered the filtered
	 * @param obj the obj
	 */
	private void filterRoles(Set<Role> foundRoles, Set<Role> filtered, UserDTO obj) {
		for (Role role : obj.getRoles()) {
			for (Role found : foundRoles) {
				if (found.getId().equals(role.getId())) {
					filtered.add(found);
				}
			}
		}
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@Transactional
	@Override
	public void delete(Long id) {
		LOG.info("method: delete");
		
		Optional<User> user = userRepository.findByIdAndStatus(id, 
				List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()))
				.map(Optional::ofNullable)
				.orElseThrow(() -> new ExceptionObjectNotFound(
						messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {"User"}, LocaleContextHolder.getLocale())
				));
		
		if (user.isEmpty()) {
			throw new ExceptionObjectNotFound(
					messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {"User"}, LocaleContextHolder.getLocale())
			);
		}
		
		
		int result = userRepository.setStatusById(Status.DELETED.getDigit(), user.get().getId());
		if (result < 1) throw new ExceptionInternalServerError(
				messageSource.getMessage("error.notDeleted", new String[] {"User"}, LocaleContextHolder.getLocale()));
	}

	/**
	 * Find all.
	 *
	 * @param pageAndSort the page and sort
	 * @return the response pagination and sort DTO
	 */
	@Override
	public ResponsePaginationAndSortDTO<UserDTO> findAll(RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: findAll");
		
		Page<User> results = userRepository
				.findByStatus(List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()), 
						PageRequest.of(pageAndSort.getPage(), pageAndSort.getSize()));
		
		List<UserDTO> mapped = results.getContent()
			.stream()
			.map(c -> {
				UserDTO dto = modelMapper.map(c, UserDTO.class);
				dto.setPassword("");
				return dto;
			})
			.collect(Collectors.toList());
		
		ResponsePaginationAndSortDTO<UserDTO> resultData = new ResponsePaginationAndSortDTO<>();
		resultData.setResult(mapped);
		resultData.setCurrentPage(results.getNumber());
		resultData.setTotalItems(results.getTotalElements());
		resultData.setTotalPages(results.getTotalPages());
		return resultData;
	}

	/**
	 * Find by term containing.
	 *
	 * @param searchTerm the search term
	 * @param pageAndSort the page and sort
	 * @return the response pagination and sort DTO
	 */
	@Override
	public ResponsePaginationAndSortDTO<UserDTO> findByTermContaining(String searchTerm,
			RequestPaginationAndSortDTO pageAndSort) {
		LOG.info("method: findByTermContaining");
		
		Page<User> results = userRepository
				.findByTermsContaining(
						searchTerm, 
						List.of(Status.ACTIVE.getDigit(), Status.INACTIVE.getDigit()), 
						PageRequest.of(pageAndSort.getPage(), pageAndSort.getSize()));
		
		List<UserDTO> mapped = results.getContent()
			.stream()
			.map(c -> {
				UserDTO dto = modelMapper.map(c, UserDTO.class);
				dto.setPassword("");
				return dto;
			})
			.collect(Collectors.toList());
		
		return new ResponsePaginationAndSortDTO<>(mapped, results.getNumber(), 
						results.getTotalElements(), results.getTotalPages());		
	}

	/**
	 * Update password.
	 *
	 * @param dto the dto
	 */
	@Transactional
	@Override
	public void updatePassword(UserPasswordChangeDTO dto) {
		LOG.info("method: updatePassword");
		
		Authentication auth = authenticationHolderProvider.provideContextHolder();		
		LOG.info((String) auth.getPrincipal());
		LOG.info((String) auth.getCredentials());
		
		if (!dto.getUsername().toUpperCase().equals(auth.getPrincipal())) throw new ExceptionValueNotPermitted(
				messageSource.getMessage("error.notPermitted", new String[] {"User"}, LocaleContextHolder.getLocale())
		);
		
		Optional<User> foundUser = userRepository
				.findByUsernameAndStatus(dto.getUsername().toUpperCase(), Status.ACTIVE.getDigit());
		
		if (foundUser.isEmpty()) throw new ExceptionValueNotPermitted(
				messageSource.getMessage(ERROR_NOT_FOUND_NAME, new String[] {"User"}, LocaleContextHolder.getLocale())
		);
		
		// It was not possible to validate OLD password
		
		int result = userRepository.setPasswordById(passwordEncoder.encode(dto.getNewPassword()), 
				foundUser.get().getId());
		if (result < 1) throw new ExceptionInternalServerError(
				messageSource.getMessage("error.passwordNotChanged", new String[] {"Password"}, LocaleContextHolder.getLocale()));
	}
	
}
