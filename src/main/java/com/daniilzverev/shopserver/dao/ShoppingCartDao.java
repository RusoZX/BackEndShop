package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByProductAndClient(@Param("productId") Long productId, @Param("userId") Long userId);
}
