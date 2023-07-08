package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, Long> {

    @Query("select i from ShoppingCart i where i.user.id = :userId and i.product.id = :productId")
    ShoppingCart findByProductAndUser(@Param("productId") Long productId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ShoppingCart i WHERE i.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
    List<ShoppingCart> findAllByUserId(@Param("userId") Long userId);
}
