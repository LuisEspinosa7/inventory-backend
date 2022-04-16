/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.dao.auth.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.lsoftware.inventory.dao.auth.ApplicationUserDao;
import com.lsoftware.inventory.entity.User;
import com.lsoftware.inventory.model.AppAuthenticationUser;
import com.lsoftware.inventory.repository.UsuarioRepository;

/**
 * The Class PostgresUserAuthenticationDao.
 * 
 * @author Luis Espinosa
 */
@Repository("authPostgres")
public class PostgresUserAuthenticationDao implements ApplicationUserDao {

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(PostgresUserAuthenticationDao.class);


	/** The usuario repository. */
	private final UsuarioRepository usuarioRepository;

	/**
	 * Instantiates a new postgres user authentication dao.
	 *
	 * @param passwordEncoder the password encoder
	 * @param usuarioRepository the usuario repository
	 */
	public PostgresUserAuthenticationDao(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Provide application user by username.
	 *
	 * @param username the username
	 * @return the optional
	 */
	@Override
	public Optional<AppAuthenticationUser> provideApplicationUserByUsername(String username) {
		LOG.info("method: provideApplicationUserByUsername");
		
		Optional<User> applicationUser = usuarioRepository.findByUsername(username);
		if (applicationUser.isEmpty()) return Optional.empty(); 
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		applicationUser.get().getRoles()
			.stream()
			.map(rol -> authorities.add(new SimpleGrantedAuthority(rol.getName())))
			.collect(Collectors.toSet());
		
		return Optional.of(new AppAuthenticationUser(username, 
				applicationUser.get().getPassword(), authorities,
				true, true, true, true));
	}

}
