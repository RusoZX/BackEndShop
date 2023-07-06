package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.*;
import com.daniilzverev.shopserver.entity.Address;
import com.daniilzverev.shopserver.entity.Order;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Sql(scripts = {"/insert_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderServiceImplTest {

    @Mock
    UserDao userDao;
    @Mock
    OrderDao orderDao;
    @Mock
    GoodsDao goodsDao;
    @Mock
    ProductDao productDao;
    @Mock
    AddressDao addressDao;
    @Mock
    JwtFilter jwtFilter;

    @InjectMocks
    OrderServiceImpl underTest;
    @Test
    void createOrder() {
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("deliveryMethod","delivery");
        requestMap.put("addressId","-1");
        requestMap.put("paymentMethod","cash");
        requestMap.put("goods","[{\"productId\":\"-1\",\"quantity\":\"1\"},{\"productId\":\"-2\",\"quantity\":\"1\"}]");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(productDao.findById(-1L)).thenReturn(Optional.of(giveTestProduct()));
        when(productDao.findById(-2L)).thenReturn(Optional.of(giveTestProduct2()));
        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.createOrder(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.ORDER_CREATED, HttpStatus.OK), response);

        verify(underTest.orderDao).save(any());
    }

    @Test
    void updateStatusPayment() {
        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("orderId","-1");
        requestMap.put("update","payment");
        requestMap.put("updateData","true");

        User user = giveTestUser();
        user.setId(-2L);
        user.setEmail("employee@example.com");
        user.setRole("employee");

        when(userDao.findByEmail("employee@example.com")).thenReturn(user);

        when(orderDao.findById(-1L)).thenReturn(Optional.of(giveTestOrder()));
        ResponseEntity<String> response= underTest.updateStatus(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK), response);

        Order order = giveTestOrder();
        order.setPaymentStatus(true);

        verify(underTest.orderDao).save(order);
    }
    @Test
    void updateStatus() {
        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("orderId","-1");
        requestMap.put("update","status");
        requestMap.put("updateData","delivered");

        User user = giveTestUser();
        user.setId(-2L);
        user.setEmail("employee@example.com");
        user.setRole("employee");

        when(userDao.findByEmail("employee@example.com")).thenReturn(user);

        when(orderDao.findById(-1L)).thenReturn(Optional.of(giveTestOrder()));
        ResponseEntity<String> response= underTest.updateStatus(requestMap);

        assertEquals(Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK), response);

        Order order = giveTestOrder();
        order.setOrderStatus("delivered");

        verify(underTest.orderDao).save(order);
    }
    private User giveTestUser(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        User user = new User();
        user.setId(-1L);
        user.setName("test");
        user.setSurname("test1");
        user.setEmail("example1@example.com");
        user.setBirthDate(LocalDate.parse("2001-09-11",formatter));
        user.setPwd("someEncryptedData");
        user.setRole("client");

        return user;
    }
    private Address giveTestAddress(){
        Address address= new Address();
        address.setId(-1L);
        address.setCountry("idk");
        address.setCity("idk");
        address.setPostalCode("idk");
        address.setStreet("idk");
        address.setHome("idk");
        address.setApartment("idk");
        address.setUser(giveTestUser());

        return address;
    }
    private Product giveTestProduct(){
        Product product = new Product();

        product.setId(-1L);
        product.setTitle("test3");
        product.setPrice(10F);
        product.setCategory("test1");
        product.setBrand("test");
        product.setColor("test2");
        product.setWeight(10F);
        product.setVolume(10F);
        product.setStock(10);

        return product;
    }
    private Product giveTestProduct2(){
        Product product = new Product();

        product.setId(-2L);
        product.setTitle("test4");
        product.setPrice(10F);
        product.setCategory("test2");
        product.setBrand("test1");
        product.setColor("test3");
        product.setWeight(10F);
        product.setVolume(10F);
        product.setStock(10);

        return product;
    }
    private Order giveTestOrder(){
        Order order = new Order();
        order.setId(-1L);
        order.setUser(giveTestUser());
        order.setPaymentMethod("cash");
        order.setDeliveryMethod("delivery");
        order.setPaymentStatus(false);
        order.setOrderStatus("pending");

        return order;
    }
}