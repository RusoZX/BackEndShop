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
@Sql(scripts = {"/insert_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductDaoTest {

    @Autowired
    ProductDao underTest;

    @Test
    void findAllInShoppingCart() {
        List<Product> expected = new ArrayList<>();

        Product product = giveTestProduct();
        product.setId(-2L);
        product.setTitle("test4");
        product.setCategory("test2");
        product.setBrand("test1");
        product.setColor("test3");

        expected.add(product);

        expected.add(giveTestProduct());

        List<Product> actualResponse = underTest.findAllInShoppingCart(-1L);

        assertEquals(expected, actualResponse);
    }
    @Test
    void findAllInShoppingCartEmpty() {
        List<Product> actualResponse = underTest.findAllInShoppingCart(-1L);

        assertTrue(actualResponse.isEmpty());
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