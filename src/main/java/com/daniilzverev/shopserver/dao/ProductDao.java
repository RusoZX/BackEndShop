package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
