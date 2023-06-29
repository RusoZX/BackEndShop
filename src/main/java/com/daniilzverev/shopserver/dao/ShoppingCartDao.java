package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, Long> {
}
