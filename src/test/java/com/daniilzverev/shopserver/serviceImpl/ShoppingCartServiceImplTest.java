package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.ProductDao;
import com.daniilzverev.shopserver.dao.ShoppingCartDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.ShoppingCart;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.wrapper.CartWrapper;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
    ShoppingCartServiceImpl underTest;

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

        ResponseEntity<String> response = underTest.addToCart(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.ITEM_ADDED + "\"}", response.getBody());
        verify(underTest.shoppingCartDao).save(any(ShoppingCart.class));
    }
    @Test
    void addToCartWithBadData() {
        Map<String, String> requestMap = new HashMap<>();

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        ResponseEntity<String> response = underTest.addToCart(requestMap);

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

        ResponseEntity<String> response = underTest.addToCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.PRODUCT_DONT_EXIST + "\"}", response.getBody());
    }
    @Test
    void addToCartWithBadQuantity() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "0");
        requestMap.put("quantity", "BadFormat");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        ResponseEntity<String> response = underTest.addToCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.INVALID_DATA + "\"}", response.getBody());
    }

    @Test
    void removeOfCartWithCorrectData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "-1");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-1L)).thenReturn(Optional.of(giveTestProduct()));

        when(shoppingCartDao.findByProductAndUser(-1L,-1L)).thenReturn(giveTestShoppingCart());
        ResponseEntity<String> response = underTest.removeOfCart(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.REMOVED + "\"}", response.getBody());

        verify(shoppingCartDao).delete(giveTestShoppingCart());
    }
    @Test
    void removeOfCartWithBadData() {
        Map<String, String> requestMap = new HashMap<>();

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-1L)).thenReturn(Optional.of(giveTestProduct()));

        when(shoppingCartDao.findByProductAndUser(-1L,-1L)).thenReturn(giveTestShoppingCart());
        ResponseEntity<String> response = underTest.removeOfCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.INVALID_DATA + "\"}", response.getBody());
    }
    @Test
    void removeOfCartWithBadFormat() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "badFormat");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-1L)).thenReturn(Optional.of(giveTestProduct()));

        when(shoppingCartDao.findByProductAndUser(-1L,-1L)).thenReturn(giveTestShoppingCart());
        ResponseEntity<String> response = underTest.removeOfCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.INVALID_DATA + "\"}", response.getBody());
    }
    @Test
    void removeOfCartWithBadProduct() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "0");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-0L)).thenReturn(Optional.empty());

        when(shoppingCartDao.findByProductAndUser(-1L,-1L)).thenReturn(giveTestShoppingCart());
        ResponseEntity<String> response = underTest.removeOfCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.PRODUCT_DONT_EXIST + "\"}", response.getBody());
    }
    @Test
    void removeAllOfCart() {
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response = underTest.removeAllOfCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.REMOVED + "\"}", response.getBody());

        verify(shoppingCartDao).deleteAllByUserId(-1L);
    }
    @Test
    void getEmptyCart() {
        when(jwtFilter.getCurrentUser()).thenReturn("example2@example.com");
        User user = giveTestUser();
        user.setId(-2L);
        when(userDao.findByEmail("example2@example.com")).thenReturn(user);

        ResponseEntity<List<CartWrapper>> response = underTest.getCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertTrue( response.getBody().isEmpty());
    }
    @Test
    void editCartWithBadProduct() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "0");
        requestMap.put("quantity","4");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-0L)).thenReturn(Optional.empty());

        when(shoppingCartDao.findByProductAndUser(-1L,-1L)).thenReturn(giveTestShoppingCart());
        ResponseEntity<String> response = underTest.editCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.PRODUCT_DONT_EXIST + "\"}", response.getBody());
    }
    @Test
    void editCartWithBadData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "0");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = underTest.editCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.INVALID_DATA + "\"}", response.getBody());
    }
    @Test
    void editCartWithNonExistingItem() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "-3");
        requestMap.put("quantity","4");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Product product = giveTestProduct();
        product.setId(-3L);

        when(productDao.findById(-3L)).thenReturn(Optional.of(product));

        when(shoppingCartDao.findByProductAndUser(-3L,-1L)).thenReturn(null);
        ResponseEntity<String> response = underTest.editCart(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.ITEM_DONT_EXIST + "\"}", response.getBody());
    }
    @Test
    void editCart() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("productId", "-1");
        requestMap.put("quantity","5");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-1L)).thenReturn(Optional.of(giveTestProduct()));

        when(shoppingCartDao.findByProductAndUser(-1L,-1L)).thenReturn(giveTestShoppingCart());
        ResponseEntity<String> response = underTest.editCart(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"" + Constants.UPDATED + "\"}", response.getBody());
        ShoppingCart item = giveTestShoppingCart();
        item.setQuantity(5);
        verify(underTest.shoppingCartDao).save(item);
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
    private ShoppingCart giveTestShoppingCart(){
        ShoppingCart cart = new ShoppingCart();
        cart.setId(-1L);
        cart.setProduct(giveTestProduct());
        cart.setUser(giveTestUser());
        cart.setQuantity(3);
        return cart;
    }

}