/*
 * Developed by: Luis Espinosa, be aware that this project
 * is part of my personal portfolio.
 */
package com.lsoftware.inventory.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import com.lsoftware.inventory.category.Category;
import com.lsoftware.inventory.exception.ExceptionValueNotPermitted;
import com.lsoftware.inventory.product.Product;
import com.lsoftware.inventory.product.ProductRepository;
import com.lsoftware.inventory.product.ProductSimpleDTO;
import com.lsoftware.inventory.shared.status.Status;
import com.lsoftware.inventory.user.User;
import com.lsoftware.inventory.user.UserSimpleDTO;

/**
 * The Class MovementServiceTest.
 * 
 * @author Luis Espinosa
 */
@ExtendWith(MockitoExtension.class)
class MovementServiceTest {
	
	/** The under test. */
	private MovementService underTest;
	
	/** The product repository. */
	@Mock
	private MovementRepository movementRepository;

	/** The model mapper. */
	private ModelMapper modelMapper;
	
	/** The message source. */
	@Mock
	private MessageSource messageSource;
	
	/** The category repository. */
	@Mock
	private ProductRepository productRepository;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		modelMapper = new ModelMapper();
		underTest = new MovementService(productRepository, movementRepository, modelMapper, messageSource);
	}
	
	/**
	 * It should add new movement INPUT action.
	 *
	 * @param type the type
	 * @param expected the expected
	 * @param productQuantity the product quantity
	 * @param movementQuantity the movement quantity
	 */
	@ParameterizedTest
	@MethodSource("movementActionsDataTestProvider")
	void itShouldAddNewMovementINPUTAction(String type, int expected, int productQuantity, int movementQuantity) {
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getCompleteProduct(productQuantity)));
		
		BDDMockito.given(movementRepository.save(any()))
			.willReturn(getMovementEntitySaved(type));
		
		MovementDTO result = underTest.add(getMovementDTO(type, movementQuantity));
		assertThat(result.getCode()).isEqualTo(getMovementEntitySaved(type).getCode());
		verify(movementRepository, times(1)).save(any());
		
		ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(productRepository).updateStock(argumentCaptor.capture(), anyLong());
		
		Integer captured = argumentCaptor.getValue();
		assertThat(captured).isEqualTo(expected);
	}
	
	/**
	 * Movement actions data test provider.
	 *
	 * @return the stream
	 */
	private static Stream<Arguments> movementActionsDataTestProvider(){
		return Stream.of(
				Arguments.of("INPUT", 25, 20, 5),
				Arguments.of("OUTPUT", 15, 20, 5)
		);
	}
	
	
	
	
	/**
	 * It should fail creating new movement stock insufficient.
	 */
	@Test
	void itShouldFailCreatingNewMovementStockInsufficient() {
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.of(getCompleteProduct(20)));
		
		assertThatThrownBy(() -> underTest.add(getMovementDTO("OUTPUT", 21)))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		
		verify(movementRepository, times(0)).save(any());
	}
	
	
	/**
	 * It should fail creating new movement product does not exists.
	 */
	@Test
	void itShouldFailCreatingNewMovementProductDoesNotExists() {
		BDDMockito.given(productRepository.findByIdAndStatus(anyLong(), anyList()))
			.willReturn(Optional.empty());
		
		assertThatThrownBy(() -> underTest.add(getMovementDTO("OUTPUT", 21)))
			.isInstanceOf(ExceptionValueNotPermitted.class);
		
		verify(movementRepository, times(0)).save(any());
	}
	
	
	/**
	 * Gets the movement DTO.
	 *
	 * @param type the type
	 * @param quantity the quantity
	 * @return the movement DTO
	 */
	private MovementDTO getMovementDTO(String type, int quantity) {
		UserSimpleDTO user = new UserSimpleDTO();
		user.setId(1L);
		
		ProductSimpleDTO product = new ProductSimpleDTO();
		product.setId(1L);
		
		MovementDetailDTO detail = new MovementDetailDTO();
		detail.setProduct(product);
		detail.setQuantity(quantity);
		
		return new MovementDTO(0L, type, user, "", List.of(detail));
	}
	
	/**
	 * Gets the complete product.
	 *
	 * @param quantity the quantity
	 * @return the complete product
	 */
	private Product getCompleteProduct(int quantity) {
		Product product = Product.builder().id(1L).name("PRODUCT 1")
				.price(new BigDecimal("12000"))
				.quantity(quantity)
				.category(new Category(1L, "CATEGORY 1", 1))
				.status(Status.ACTIVE.getDigit())
				.build();
		
		return product;
	}
	
	/**
	 * Gets the movement entity saved.
	 *
	 * @param type the type
	 * @return the movement entity saved
	 */
	private Movement getMovementEntitySaved(String type) {
		User user = new User();
		user.setId(1L);
		
		Movement movement = Movement.builder()
				.id(1L).timestamp(LocalDateTime.now()).type(type)
				.user(user)
				.code("22260ef0-cb22-11ec-9d64-0242ac120002")
				.details(null)
				.build();
				
		return movement; 
	}

}
