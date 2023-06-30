package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.id in (select i.product.id from ShoppingCart i where i.user.id= :userId)")
    List<Product> findAllInShoppingCart( @Param("userId") Long userId);
}
