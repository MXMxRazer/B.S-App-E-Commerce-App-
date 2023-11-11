package ca.sheridancollege.tamrakar;

import ca.sheridancollege.tamrakar.beans.CheckUser;
import ca.sheridancollege.tamrakar.beans.Product;
import ca.sheridancollege.tamrakar.beans.RegisterUser;
import ca.sheridancollege.tamrakar.beans.User;
import ca.sheridancollege.tamrakar.controller.ProductController;
import ca.sheridancollege.tamrakar.database.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class Assignment3ApplicationTests {

	@InjectMocks
	private ProductController productController;

	@InjectMocks
	private ProductRepository productRepository;

	@Mock
	private Model model;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		Product product = new Product(1, "watch", "a smart watch", 1, 20);

		Mockito.when(productRepository.deleteProduct(product.getId()))
				.thenReturn(1);
	}

	@Test
	void testGetUserByUserName() {
		String username = "testUser";
		List<User> mockUsers = Arrays.asList(
				new User(1L, "testUser", "password", "seller")
		);

		when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(BeanPropertyRowMapper.class)))
				.thenReturn(mockUsers);

		List<User> result = productRepository.getUserByUserName(username);

		verify(jdbcTemplate).query(anyString(), any(MapSqlParameterSource.class), any(BeanPropertyRowMapper.class));
		assertEquals(mockUsers, result);
	}

	@Test
	void testInsertUser() {
		RegisterUser mockUser = new RegisterUser();

		when(jdbcTemplate.update(anyString(), (SqlParameterSource) any())).thenReturn(1);

		int result = productRepository.insertUser(mockUser);

		verify(jdbcTemplate).update(anyString(), (SqlParameterSource) any());
		assertEquals(1, result);
	}


	@Test
	void testDeleteProduct() {
		int productId = 1;

		when(jdbcTemplate.update(anyString(), (SqlParameterSource) any())).thenReturn(1);

		productRepository.deleteProduct(productId);

		verify(jdbcTemplate).update(anyString(), (SqlParameterSource) any());
	}

	@Test
	void testLoadRootSeller() {
		ProductController productController = new ProductController();

		Model model = mock(Model.class);

		String result = productController.loadRootSeller();

		assertEquals("homeSeller.html", result);
	}

	@Test
	void testLoadRootBuyer() {
		ProductController productController = new ProductController();

		Model model = mock(Model.class);

		String result = productController.loadRootBuyer();

		assertEquals("homeBuyer.html", result);
	}

	@Test
	void testGoAddProducts() {
		// Mock data
		Model mockModel = mock(Model.class);

		// Test
		String result = productController.goAddProducts(mockModel);

		// Verify and assert
		verify(mockModel).addAttribute(eq("product"), any(Product.class));
		assertEquals("addProduct.html", result);
	}

	@Test
	void testInvalidPage() {
		// Test
		String result = productController.invalidPage();

		// Assert
		assertEquals("invalid", result);
	}



}
