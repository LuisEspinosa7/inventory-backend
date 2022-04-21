package com.lsoftware.inventory.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.shared.status.Status;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
	
	private CategoryService underTest;
	
	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private MessageSource messageSource;
	
	
	@BeforeEach
	void setUp() throws Exception {
		underTest = new CategoryService(categoryRepository, modelMapper, messageSource);
	}

	@Test
	void itShouldAddNewCategory() {
		String name = "Category1";
		
		CategoryDTO dto = new CategoryDTO();
		dto.setName(name);
		
		Category category = new Category();
		category.setName(name);
		
		BDDMockito.given(categoryRepository.findByNameAndStatus(anyString(), anyInt()))
			.willReturn(Optional.empty());
		
		BDDMockito.given(modelMapper.map(any(), eq(Category.class)))
			.willReturn(category);
		
		category.setId(1L);
		category.setStatus(Status.ACTIVE.getDigit());
		BDDMockito.given(categoryRepository.save(any()))
			.willReturn(category);
		
		dto.setId(1L);
		dto.setStatus(Status.ACTIVE.getDigit());
		dto.setName("CATEGORY1");
		BDDMockito.given(modelMapper.map(any(), eq(CategoryDTO.class)))
			.willReturn(dto);
		
		CategoryDTO result = underTest.add(dto);
		assertThat(result.getName()).isEqualTo(dto.getName().toUpperCase());
		assertThat(result.getId()).isEqualTo(dto.getId());
		assertThat(result.getStatus()).isEqualTo(dto.getStatus());
		verify(categoryRepository, times(1)).save(any());
	}
	
	@Test
	void itShouldFailCategoryAlreadyExist() {
		Category category = new Category();
		category.setName("CATEGORY1");
		category.setId(1L);
		category.setStatus(Status.ACTIVE.getDigit());
		
		CategoryDTO dto = new CategoryDTO();
		dto.setName("CATEGORY1");
		
		BDDMockito.given(categoryRepository.findByNameAndStatus(Mockito.anyString(), anyInt()))
			.willReturn(Optional.of(category));
		
		assertThatThrownBy(() -> underTest.add(dto))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		
		verify(categoryRepository, times(0)).save(any());
	}

}
