package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.wrapper.CartWrapper;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/insert_test_data_product_dao.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_product_dao.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductDaoTest {

    @Autowired
    ProductDao underTest;

    @Test
    void findAllInShoppingCart() {
        List<CartWrapper> expected = new ArrayList<>();

        CartWrapper item = new CartWrapper();
        item.setId(-2L);
        item.setTitle("test4");
        item.setPrice(2F);
        item.setQuantity(4);

        expected.add(item);

        item = new CartWrapper();
        item.setId(-1L);
        item.setTitle("test3");
        item.setPrice(1F);
        item.setQuantity(3);

        expected.add(item);

        List<CartWrapper> actualResponse = underTest.findAllInShoppingCart(-1L);

        assertEquals(expected, actualResponse);
    }
    @Test
    void findAllInShoppingCartEmpty() {
        List<CartWrapper> actualResponse = underTest.findAllInShoppingCart(-2L);

        assertTrue(actualResponse.isEmpty());
    }
    @Test
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


        List<ProductWrapper> actualResponse = underTest.findAllByNone(PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findAllLimit5(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-15L,"title",15F,10));
        expected.add(new ProductWrapper(-14L,"title",14F,10));
        expected.add(new ProductWrapper(-13L,"title",13F,10));
        expected.add(new ProductWrapper(-12L,"title",12F,10));
        expected.add(new ProductWrapper(-11L,"title",11F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByNone(PageRequest.of(0, 5));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByCategory(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-9L,"test3",9F,10));
        expected.add(new ProductWrapper(-8L,"test3",8F,10));
        expected.add(new ProductWrapper(-7L,"test3",7F,10));
        expected.add(new ProductWrapper(-6L,"test3",6F,10));
        expected.add(new ProductWrapper(-5L,"test3",5F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByCategory("category",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByOtherCategory(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-1L,"test3",1F,10));

        List<ProductWrapper> actualResponse = underTest.findAllByCategory("test1",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByPriceDesc(){
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


        List<ProductWrapper> actualResponse = underTest.findAllByPriceDesc(PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findAllByPriceDescLimit5(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-15L,"title",15F,10));
        expected.add(new ProductWrapper(-14L,"title",14F,10));
        expected.add(new ProductWrapper(-13L,"title",13F,10));
        expected.add(new ProductWrapper(-12L,"title",12F,10));
        expected.add(new ProductWrapper(-11L,"title",11F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByPriceDesc(PageRequest.of(0, 5));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByPriceAsc(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-1L,"test3",1F,10));
        expected.add(new ProductWrapper(-2L,"test4",2F,10));
        expected.add(new ProductWrapper(-3L,"test4",3F,10));
        expected.add(new ProductWrapper(-4L,"test4",4F,10));
        expected.add(new ProductWrapper(-5L,"test3",5F,10));
        expected.add(new ProductWrapper(-6L,"test3",6F,10));
        expected.add(new ProductWrapper(-7L,"test3",7F,10));
        expected.add(new ProductWrapper(-8L,"test3",8F,10));
        expected.add(new ProductWrapper(-9L,"test3",9F,10));
        expected.add(new ProductWrapper(-10L,"test4",10F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByPriceAsc(PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findAllByPriceAscLimit5(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-1L,"test3",1F,10));
        expected.add(new ProductWrapper(-2L,"test4",2F,10));
        expected.add(new ProductWrapper(-3L,"test4",3F,10));
        expected.add(new ProductWrapper(-4L,"test4",4F,10));
        expected.add(new ProductWrapper(-5L,"test3",5F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByPriceAsc(PageRequest.of(0, 5));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByBrand(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-6L,"test3",6F,10));
        expected.add(new ProductWrapper(-5L,"test3",5F,10));
        expected.add(new ProductWrapper(-4L,"test4",4F,10));
        expected.add(new ProductWrapper(-3L,"test4",3F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByBrand("brand",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByOtherBrand(){
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
        expected.add(new ProductWrapper(-2L,"test4",2F,10));

        List<ProductWrapper> actualResponse = underTest.findAllByBrand("test1",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByColor(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-12L,"title",12F,10));
        expected.add(new ProductWrapper(-11L,"title",11F,10));
        expected.add(new ProductWrapper(-10L,"test4",10F,10));
        expected.add(new ProductWrapper(-9L,"test3",9F,10));
        expected.add(new ProductWrapper(-8L,"test3",8F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByColor("color",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByOtherColor(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-15L,"title",15F,10));
        expected.add(new ProductWrapper(-14L,"title",14F,10));
        expected.add(new ProductWrapper(-13L,"title",13F,10));
        expected.add(new ProductWrapper(-7L,"test3",7F,10));
        expected.add(new ProductWrapper(-6L,"test3",6F,10));
        expected.add(new ProductWrapper(-5L,"test3",5F,10));
        expected.add(new ProductWrapper(-4L,"test4",4F,10));
        expected.add(new ProductWrapper(-3L,"test4",3F,10));
        expected.add(new ProductWrapper(-2L,"test4",2F,10));

        List<ProductWrapper> actualResponse = underTest.findAllByColor("test3",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByTitle(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-15L,"title",15F,10));
        expected.add(new ProductWrapper(-14L,"title",14F,10));
        expected.add(new ProductWrapper(-13L,"title",13F,10));
        expected.add(new ProductWrapper(-12L,"title",12F,10));
        expected.add(new ProductWrapper(-11L,"title",11F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByTitle("title",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findByOtherTitle(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-10L,"test4",10F,10));
        expected.add(new ProductWrapper(-4L,"test4",4F,10));
        expected.add(new ProductWrapper(-3L,"test4",3F,10));
        expected.add(new ProductWrapper(-2L,"test4",2F,10));

        List<ProductWrapper> actualResponse = underTest.findAllByTitle("test4",PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findBestSellers10(){
        List<ProductWrapper> expected = new ArrayList<>();

        expected.add(new ProductWrapper(-12L,"title",12F,10));
        expected.add(new ProductWrapper(-15L,"title",15F,10));
        expected.add(new ProductWrapper(-14L,"title",14F,10));
        expected.add(new ProductWrapper(-11L,"title",11F,10));
        expected.add(new ProductWrapper(-3L,"test4",3F,10));
        expected.add(new ProductWrapper(-10L,"test4",10F,10));
        expected.add(new ProductWrapper(-9L,"test3",9F,10));
        expected.add(new ProductWrapper(-8L,"test3",8F,10));
        expected.add(new ProductWrapper(-2L,"test4",2F,10));
        expected.add(new ProductWrapper(-1L,"test3",1F,10));


        List<ProductWrapper> actualResponse = underTest.findAllByBestSellers(PageRequest.of(0, 10));

        assertEquals(expected, actualResponse);
    }
    @Test
    void findAllCategories(){
        List<String> expected = new ArrayList<>();
        expected.add("test2");
        expected.add("category");
        expected.add("test1");

        List<String> actualResponse = underTest.findAllCategories();

        assertEquals(expected, actualResponse);

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