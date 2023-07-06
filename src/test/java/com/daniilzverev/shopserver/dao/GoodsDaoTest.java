package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/insert_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

class GoodsDaoTest {
    @Autowired
    GoodsDao underTest;
    @Test
    void findAllByOrderId() {
        List<Goods> expected = new ArrayList<>();
        Goods item = new Goods();
        item.setOrder(giveTestOrder());
        item.setQuantity(1);
        item.setProduct(giveTestProduct2());
        item.setId(-2L);
        expected.add(item);
        item = new Goods();
        item.setOrder(giveTestOrder());
        item.setQuantity(1);
        item.setProduct(giveTestProduct());
        item.setId(-1L);
        expected.add(item);

        assertEquals(expected, underTest.findAllByOrderId(-1L));
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
        order.setAddress(giveTestAddress());
        order.setDeliveryMethod("delivery");
        order.setPaymentStatus(false);
        order.setOrderStatus("pending");

        return order;
    }
}