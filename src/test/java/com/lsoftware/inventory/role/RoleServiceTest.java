/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

/**
 * The Class RoleServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
	
	/** The under test. */
	private RoleService underTest;
	
	/** The role repository. */
	@Mock
	private RoleRepository roleRepository;
	
	/** The model mapper. */
	@Mock
	ModelMapper modelMapper;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new RoleService(roleRepository, modelMapper);
	}

	/**
	 * It should list roles.
	 */
	@Test
	void itShouldListRoles() {
		
		BDDMockito.given(roleRepository.findAll()).willReturn(
				List.of(
						new Role(1L, "ADMIN", "DESCRIPTION"),
						new Role(2L, "SUPERVISOR", "DESCRIPTION")
				)
		);
		
		// If you need to mock the model mapper, it is quite easy in comparison to mocking ObjectMappper from jackson.
		// BDDMockito.given(modelMapper.map(any(), any())).willReturn(new RoleDTO(3L, "EXAMPLE", "TEST"));
		
		List<RoleDTO> roles = underTest.list();
		verify(roleRepository, times(1)).findAll();
		
		assertThat(roles.size()).isEqualTo(2);
	}

}
