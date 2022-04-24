/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lsoftware.inventory.authentication.AuthenticationHolderProvider;
import com.lsoftware.inventory.exception.ExceptionInternalServerError;
import com.lsoftware.inventory.exception.ExceptionObjectNotFound;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.role.Role;
import com.lsoftware.inventory.shared.request.RequestPaginationAndSortDTO;
import com.lsoftware.inventory.shared.response.ResponsePaginationAndSortDTO;
import com.lsoftware.inventory.shared.status.Status;

/**
 * The Class UserServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	/** The Constant ENCODED_PASSWORD. */
	private static final String ENCODED_PASSWORD = "$2a$10$begExMKlRQ8aFFx1fRV64Ogbm7J3KuAQzuYlnhM0gnNvzUSrxz5DS";
	
	/** The under test. */
	private UserService underTest;
	
	/** The category repository. */
	@Mock
	private UserRepository userRepository;

	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/** The message source. */
	@Mock
	private MessageSource messageSource;
	
	/** The password encoder. */
	@Mock
	private PasswordEncoder passwordEncoder;
	
	/** The authentication holder provider. */
	@Mock
	private AuthenticationHolderProvider authenticationHolderProvider;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		modelMapper = new ModelMapper();
		underTest = new UserService(userRepository, modelMapper, messageSource, passwordEncoder, authenticationHolderProvider);
	}

	/**
	 * It should add new user.
	 */
	@Test
	void itShouldAddNewUser() {
		
		BDDMockito.given(userRepository.findByDocumentAndUsernameAndStatus(anyString(), anyString(), anyList()))
			.willReturn(Optional.empty());
			
		BDDMockito.given(passwordEncoder.encode(anyString()))
			.willReturn(ENCODED_PASSWORD);
		
		BDDMockito.given(userRepository.save(any()))
			.willReturn(getUserEntitySaved());
		
		UserDTO result = underTest.add(getUserDTO());
		assertThat(result.getUsername()).isEqualTo(getUserEntitySaved().getUsername());
		assertThat(result.getId()).isEqualTo(getUserEntitySaved().getId());
		assertThat(result.getStatus()).isEqualTo(getUserEntitySaved().getStatus());
		verify(userRepository, times(1)).save(any());
	}
	
	/**
	 * It should fail creating user already exist.
	 */
	@Test
	void itShouldFailCreatingUserAlreadyExist() {
		
		BDDMockito.given(userRepository.findByDocumentAndUsernameAndStatus(anyString(), anyString(), anyList()))
		.willReturn(Optional.of(getUserEntitySaved()));
		
		assertThatThrownBy(() -> underTest.add(getUserDTO()))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		
		verify(userRepository, times(0)).save(any());
	}
	
	
	/**
	 * It should update user.
	 */
	@Test
	void itShouldUpdateUser() {
		
		BDDMockito.given(userRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getUserEntitySaved()));
		
		BDDMockito.given(userRepository.save(any()))
			.willReturn(getUserEntitySaved());
		
		UserDTO result = underTest.update(getUserDTO());
		assertThat(result.getUsername()).isEqualTo(getUserEntitySaved().getUsername());
		assertThat(result.getId()).isEqualTo(getUserEntitySaved().getId());
		assertThat(result.getStatus()).isEqualTo(getUserEntitySaved().getStatus());
		verify(userRepository, times(1)).save(any());
	}
	
	
	/**
	 * It should not update user does not exists.
	 */
	@Test
	void itShouldNotUpdateUserDoesNotExists() {
		
		BDDMockito.given(userRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.empty());
		
		assertThatThrownBy(() -> underTest.update(getUserDTO()))
		.isInstanceOf(ExceptionValueNotPermitted.class);
	
		verify(userRepository, times(0)).save(any());
	}
	
	
	/**
	 * It should delete user.
	 */
	@Test
	void itShouldDeleteUser() {
		
		BDDMockito.given(userRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getUserEntitySaved()));

		BDDMockito.given(userRepository.setStatusById(anyInt(), anyLong()))
		.willReturn(1);
		
		underTest.delete(1L);
		verify(userRepository, times(1)).setStatusById(anyInt(), anyLong());
	}
	
	
	/**
	 * It should fail deleting user status not modified.
	 */
	@Test
	void itShouldFailDeletingUserStatusNotModified() {
	
		BDDMockito.given(userRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getUserEntitySaved()));

		BDDMockito.given(userRepository.setStatusById(anyInt(), anyLong()))
			.willReturn(0);
		
		assertThatThrownBy(() -> underTest.delete(1L))
			.isInstanceOf(ExceptionInternalServerError.class);
		
		verify(userRepository, times(1)).setStatusById(anyInt(), anyLong());
	}
	
	
	/**
	 * It should fail deleting user not found.
	 */
	@Test
	void itShouldFailDeletingUserNotFound() {
		
		BDDMockito.given(userRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.empty());
		
		assertThatThrownBy(() -> underTest.delete(1L))
			.isInstanceOf(ExceptionObjectNotFound.class);
		
		verify(userRepository, times(0)).setStatusById(anyInt(), anyLong());
	}
	
	
	
	/**
	 * It should list users by status and pageable.
	 */
	@Test
	void itShouldListUsersByStatusAndPageable(){
		
		Page<User> page = new PageImpl<>(
				List.of(
						getUserEntitySaved(),
						new User(2L, "987654321", "Valen", "Espinosa", "VALEN4", "123456", Status.ACTIVE.getDigit(), 
								Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")))
				)
		);
		
		BDDMockito.given(userRepository.findByStatus(anyList(), any()))
			.willReturn(page);
		
		RequestPaginationAndSortDTO request = new RequestPaginationAndSortDTO();
		request.setPage(0);
		request.setSize(2);
		ResponsePaginationAndSortDTO<UserDTO> results = underTest.findAll(request);
		assertThat(results.getResult().size()).isEqualTo(2);
	}
	
	
	/**
	 * It should list users by status and pageable and search term.
	 */
	@Test
	void itShouldListUsersByStatusAndPageableAndSearchTerm(){
		
		Page<User> page = new PageImpl<>(
				List.of(
						new User(2L, "987654321", "Valen", "Espinosa", "VALEN4", "123456", Status.ACTIVE.getDigit(), 
								Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")))
				)
		);
		
		BDDMockito.given(userRepository.findByTermsContaining(anyString(), anyList(), any()))
			.willReturn(page);
		
		RequestPaginationAndSortDTO request = new RequestPaginationAndSortDTO();
		request.setPage(0);
		request.setSize(2);
		ResponsePaginationAndSortDTO<UserDTO> results = underTest.findByTermContaining("Val", request);
		assertThat(results.getResult().size()).isEqualTo(1);
	}
	
	/**
	 * It should update password.
	 */
	@Test
	void itShouldUpdatePassword() {
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("LUIS3", "123456");
		
		BDDMockito.given(authenticationHolderProvider.provideContextHolder())
			.willReturn(auth);
		
		BDDMockito.given(userRepository.findByUsernameAndStatus(anyString(), anyInt()))
			.willReturn(Optional.of(getUserEntitySaved()));
		
		BDDMockito.given(passwordEncoder.encode(anyString()))
			.willReturn("12345678");
		
		BDDMockito.given(userRepository.setPasswordById(anyString(), anyLong()))
			.willReturn(1);
		
		underTest.updatePassword(getUserPasswordDTO());
		verify(userRepository, times(1)).setPasswordById(anyString(), anyLong());
	}
	
	
	/**
	 * It should not update password not saved in DB.
	 */
	@Test
	void itShouldNotUpdatePasswordNotSavedInDB() {
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("LUIS3", "123456");
		
		BDDMockito.given(authenticationHolderProvider.provideContextHolder())
			.willReturn(auth);
		
		BDDMockito.given(userRepository.findByUsernameAndStatus(anyString(), anyInt()))
			.willReturn(Optional.of(getUserEntitySaved()));
		
		BDDMockito.given(passwordEncoder.encode(anyString()))
			.willReturn("12345678");
		
		BDDMockito.given(userRepository.setPasswordById(anyString(), anyLong()))
			.willReturn(0);
		
		assertThatThrownBy(() -> underTest.updatePassword(getUserPasswordDTO()))
			.isInstanceOf(ExceptionInternalServerError.class);
		verify(userRepository, times(1)).setPasswordById(anyString(), anyLong());
	}
	
	
	/**
	 * It should not update passwor user not found.
	 */
	@Test
	void itShouldNotUpdatePassworUserNotFound() {
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("LUIS3", "123456");
		
		BDDMockito.given(authenticationHolderProvider.provideContextHolder())
			.willReturn(auth);
		
		BDDMockito.given(userRepository.findByUsernameAndStatus(anyString(), anyInt()))
			.willReturn(Optional.empty());
		
		assertThatThrownBy(() -> underTest.updatePassword(getUserPasswordDTO()))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		verify(userRepository, times(0)).setPasswordById(anyString(), anyLong());
	}
	
	
	/**
	 * It should not update passwor not same credentials.
	 */
	@Test
	void itShouldNotUpdatePassworNotSameCredentials() {
		
		UsernamePasswordAuthenticationToken auth = 
				new UsernamePasswordAuthenticationToken("LUIS3", "1234567");
		
		BDDMockito.given(authenticationHolderProvider.provideContextHolder())
			.willReturn(auth);
		
		assertThatThrownBy(() -> underTest.updatePassword(getUserPasswordDTO()))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		verify(userRepository, times(0)).setPasswordById(anyString(), anyLong());
	}
	
	/**
	 * It should not update passwor not same principal.
	 */
	@Test
	void itShouldNotUpdatePassworNotSamePrincipal() {
		
		UsernamePasswordAuthenticationToken auth = 
				new UsernamePasswordAuthenticationToken("LUIS4", "123456");
		
		BDDMockito.given(authenticationHolderProvider.provideContextHolder())
			.willReturn(auth);
		
		assertThatThrownBy(() -> underTest.updatePassword(getUserPasswordDTO()))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		verify(userRepository, times(0)).setPasswordById(anyString(), anyLong());
	}
	
	
	/**
	 * Gets the user DTO.
	 *
	 * @return the user DTO
	 */
	private UserDTO getUserDTO() {
		return new UserDTO(1L, "123456789", "Luis", "Espinosa", "luis3", "123456", Status.ACTIVE.getDigit(), 
				Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")));
	}
	
	/**
	 * Gets the user entity saved.
	 *
	 * @return the user entity saved
	 */
	private User getUserEntitySaved() {
		return new User(1L, "123456789", "Luis", "Espinosa", "LUIS3", "123456", Status.ACTIVE.getDigit(), 
				Set.of(new Role(1L, "ADMIN", "DESCCRIPTION")));
	}
	
	/**
	 * Gets the user password DTO.
	 *
	 * @return the user password DTO
	 */
	private UserPasswordChangeDTO getUserPasswordDTO() {
		return new UserPasswordChangeDTO("LUIS3", "123456", "12345678");
	}

}
