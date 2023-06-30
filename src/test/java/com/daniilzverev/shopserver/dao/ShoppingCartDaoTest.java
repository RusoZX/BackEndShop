package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.ShoppingCart;
import com.daniilzverev.shopserver.entity.User;
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
@Sql(scripts = {"/insert_test_data_shopping_cart.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_shopping_cart.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartDaoTest {

    @Autowired
    ShoppingCartDao underTest;

    @Test
    void findByProductAndUser() {
        ShoppingCart expected = new ShoppingCart();
        expected.setId(-1L);
        expected.setProduct(giveTestProduct());
        expected.setUser(giveTestUser());
        expected.setQuantity(3);

        ShoppingCart actualResponse = underTest.findByProductAndUser(-1L,-1L);

        assertEquals(expected,actualResponse);

    }
    @Test
    void deleteAllByUserId(){
        underTest.deleteAllByUserId(-1L);

        List<ShoppingCart> response= underTest.findAllByUserId(-1L);

        assertTrue(response.isEmpty());
    }
    @Test
    void findAllByUserId() {
        List<ShoppingCart> expected = new ArrayList<ShoppingCart>();
        ShoppingCart cart = new ShoppingCart();
        cart.setId(-2L);
        cart.setUser(giveTestUser());

        Product product = giveTestProduct();
        product.setId(-2L);
        product.setTitle("test4");
        product.setCategory("test2");
        product.setBrand("test1");
        product.setColor("test3");
        cart.setProduct(product);
        cart.setQuantity(4);

        expected.add(cart);

        cart = new ShoppingCart();
        cart.setId(-1L);
        cart.setUser(giveTestUser());
        cart.setProduct(giveTestProduct());
        cart.setQuantity(3);

        expected.add(cart);

        List<ShoppingCart> actualResponse = underTest.findAllByUserId(-1L);

        assertEquals(expected, actualResponse);

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