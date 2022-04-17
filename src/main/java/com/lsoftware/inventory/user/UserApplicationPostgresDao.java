/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

/**
 * The Class UserApplicationPostgresDao.
 * 
 * @author Luis Espinosa
 */
@Repository("authPostgres")
public class UserApplicationPostgresDao implements UserApplicationProviderDao {

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(UserApplicationPostgresDao.class);


	/** The usuario repository. */
	private final UserRepository usuarioRepository;

	/**
	 * Instantiates a new postgres user authentication dao.
	 *
	 * @param usuarioRepository the usuario repository
	 */
	public UserApplicationPostgresDao(UserRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Provide application user by username.
	 *
	 * @param username the username
	 * @return the optional
	 */
	@Override
	public Optional<UserAuthentication> provideApplicationUserByUsername(String username) {
		LOG.info("method: provideApplicationUserByUsername");
		
		Optional<User> applicationUser = usuarioRepository.findByUsername(username);
		if (applicationUser.isEmpty()) return Optional.empty(); 
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		applicationUser.get().getRoles()
			.stream()
			.map(rol -> authorities.add(new SimpleGrantedAuthority(rol.getName())))
			.collect(Collectors.toSet());
		
		return Optional.of(new UserAuthentication(username, 
				applicationUser.get().getPassword(), authorities,
				true, true, true, true));
	}

}
