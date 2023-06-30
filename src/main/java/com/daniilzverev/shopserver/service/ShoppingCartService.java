package com.daniilzverev.shopserver.service;

import com.daniilzverev.shopserver.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ResponseEntity<String> addToCart(Map<String, String> requestMap);
    ResponseEntity<String> removeOfCart(Map<String, String> requestMap);
    ResponseEntity<String> removeAllOfCart();
    ResponseEntity<List<Product>> getCart();
    ResponseEntity<String> editCart(Map<String, String> requestMap);
}
