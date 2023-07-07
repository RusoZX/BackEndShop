package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.ProductDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.service.ProductService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest

@Sql(scripts = {"/insert_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private ProductDao productDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private ProductServiceImpl underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProductSuccess() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.addProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.PRODUCT_ADDED, HttpStatus.OK), result);

    }
    @Test
    void addProductWithoutEmployee() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        User user = giveTestUser();
        user.setId(-1L);
        user.setEmail("example1@example.com");
        user.setRole("client");

        when(userDao.findByEmail("example1@example.com")).thenReturn(user);

        ResponseEntity<String> result = underTest.addProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED), result);

    }
    @Test
    void addProductBadFormat() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("stock","4");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.addProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST), result);

    }
    @Test
    void addProductBadNumberFormat() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","badFormat");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.addProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST), result);
    }

    @Test
    void editProductSuccess() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-3");
        requestMap.put("price","4.20");


        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.editProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK), result);
        Product product = giveTestProduct();
        product.setPrice(4.20F);
        verify(productDao).save(product);

    }
    @Test
    void editProductWithoutEmployee() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("price","4.20");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        User user = giveTestUser();
        user.setId(-1L);
        user.setEmail("example1@example.com");
        user.setRole("client");

        when(userDao.findByEmail("example1@example.com")).thenReturn(user);

        ResponseEntity<String> result = underTest.editProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED), result);

    }
    @Test
    void editProductBadFormat() {
        Map<String,String> requestMap= new HashMap<>();


        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.editProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST), result);

    }
    @Test
    void editProductBadNumberFormat() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("price","badFormat");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.editProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST), result);
    }

    @Test
    void removeProductSuccess() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-3");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.removeProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.REMOVED, HttpStatus.OK), result);
        verify(productDao).delete(giveTestProduct());

    }
    @Test
    void deleteProductWithoutEmployee() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        User user = giveTestUser();
        user.setId(-1L);
        user.setEmail("example1@example.com");
        user.setRole("client");

        when(userDao.findByEmail("example1@example.com")).thenReturn(user);

        ResponseEntity<String> result = underTest.removeProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED), result);

    }
    @Test
    void removeProductBadFormat() {
        Map<String,String> requestMap= new HashMap<>();


        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.removeProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST), result);

    }
    @Test
    void removeProductBadNumberFormat() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> result = underTest.editProduct(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST), result);
    }

    @Test
    void getProductSuccessfully() {
        //Given an id
        Long id = -3L;


        when(productDao.findById(-3L)).thenReturn(Optional.of(giveTestProduct()));

        ResponseEntity<Product> result = underTest.getProduct(id);

        assertEquals(HttpStatus.OK,result.getStatusCode());
    }
    @Test
    void getProductUnexistent() {
        //Given an id
        Long id = 0L;


        when(productDao.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<Product> result = underTest.getProduct(id);

        assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());
    }
    @Test
    void getProductBadFormat() {
        //When the Id is given in a bad format inside the url it will be always null
        Long id = null;

        ResponseEntity<Product> result = underTest.getProduct(id);

        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
    @Test
    void changeCategories() {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productList","[{\"id\":\"-15\"},{\"id\":\"-14\"}]");
        requestMap.put("newCategory","newCategory");

        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        when(userDao.findByEmail("employee@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-15L)).thenReturn(Optional.of(giveTestProduct2()));
        Product product = giveTestProduct2();
        product.setId(-14L);
        product.setPrice(-14F);
        when(productDao.findById(-14L)).thenReturn(Optional.of(product));

        ResponseEntity<String> result = underTest.changeCategories(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK), result);
        product = giveTestProduct2();
        product.setCategory("newCategory");
        verify(underTest.productDao).save(product);
        product = giveTestProduct2();
        product.setCategory("newCategory");
        product.setId(-14L);
        product.setPrice(-14F);
        verify(underTest.productDao).save(product);
    }
    //Ask if it is worth to test get in here since you have to mock the productDAo
    /*@Test
    void findAllLimit10(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-15L,"title",15F,10));
        expected.add(new ProductWrapper(-14L,"title",14F,10));
        expected.add(new ProductWrapper(-13L,"title",13F,10));
        expected.add(new ProductWrapper(-12L,"title",12F,10));
        expected.add(new ProductWrapper(-11L,"title",11F,10));
        expected.add(new ProductWrapper(-10L,"test4",10F,10));
        expected.add(new ProductWrapper(-9L,"test3",9F,10));
        expected.add(new ProductWrapper(-8L,"test3",8F,10));
        expected.add(new ProductWrapper(-7L,"test3",7F,10));
        expected.add(new ProductWrapper(-6L,"test3",6F,10));


        ResponseEntity<List<ProductWrapper>> actualResponse = underTest.getProducts("None", "10",null);

        assertEquals(expected, actualResponse.getBody());
    }
    */

    private User giveTestUser(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        User user = new User();
        user.setId(-2L);
        user.setName("test");
        user.setSurname("test1");
        user.setEmail("employee@example.com");
        user.setBirthDate(LocalDate.parse("2001-09-11",formatter));
        user.setPwd("someEncryptedData");
        user.setRole("employee");

        return user;
    }
    private Product giveTestProduct(){
        Product product = new Product();

        product.setId(-3L);
        product.setTitle("idk");
        product.setPrice(3.14F);
        product.setCategory("idk1");
        product.setBrand("idk2");
        product.setColor("red");
        product.setWeight(10.4F);
        product.setVolume(10.5F);
        product.setStock(4);

        return product;
    }
    private Product giveTestProduct2(){
        Product product = new Product();

        product.setId(-15L);
        product.setTitle("title");
        product.setPrice(15F);
        product.setCategory("test2");
        product.setBrand("test1");
        product.setColor("test3");
        product.setWeight(10F);
        product.setVolume(10F);
        product.setStock(10);

        return product;
    }
}