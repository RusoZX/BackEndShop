package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.ProductDao;
import com.daniilzverev.shopserver.dao.ShoppingCartDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.ShoppingCart;
import com.daniilzverev.shopserver.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Sql(scripts = {"/insert_test_data_shopping_cart.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_shopping_cart.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private ShoppingCartDao shoppingCartDao;
    @Mock
    private JwtFilter jwtFilter;

    @InjectMocks
    ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCartWithCorrectData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "-1");
        requestMap.put("quantity", "3");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-1L)).thenReturn(Optional.of(giveTestProduct()));

        ResponseEntity<String> response = shoppingCartService.addToCart(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.ITEM_ADDED + "\"}", response.getBody());
    }
    @Test
    void addToCartWithBadData() {
        Map<String, String> requestMap = new HashMap<>();

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        ResponseEntity<String> response = shoppingCartService.addToCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.INVALID_DATA + "\"}", response.getBody());
    }
    @Test
    void addToCartWithBadProduct() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "0");
        requestMap.put("quantity", "3");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = shoppingCartService.addToCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.PRODUCT_DONT_EXIST + "\"}", response.getBody());
    }
    @Test
    void addToCartWithBadQuantity() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "0");
        requestMap.put("quantity", "BadFormat");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        ResponseEntity<String> response = shoppingCartService.addToCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.INVALID_DATA + "\"}", response.getBody());
    }


    private User giveTestUser() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        User user = new User();
        user.setId(-1L);
        user.setName("test");
        user.setSurname("test1");
        user.setEmail("example1@example.com");
        user.setBirthDate(LocalDate.parse("2001-09-11", formatter));
        user.setPwd("someEncryptedData");
        user.setRole("client");

        return user;
    }

    private Product giveTestProduct() {
        Product product = new Product();
        product.setId(-1L);
        product.setBrand("test");
        product.setCategory("test1");
        product.setColor("test2");
        product.setPrice(10f);
        product.setPrice(10f);
        product.setStock(10);
        product.setTitle("test3");
        product.setVolume(10f);
        product.setWeight(10f);

        return product;
    }

}