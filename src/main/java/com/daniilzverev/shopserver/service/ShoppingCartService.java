package com.daniilzverev.shopserver.service;

import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.wrapper.CartWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ResponseEntity<String> addToCart(Map<String, String> requestMap);
    ResponseEntity<String> removeOfCart(Map<String, String> requestMap);
    ResponseEntity<String> removeAllOfCart();
    ResponseEntity<List<CartWrapper>> getCart();
    ResponseEntity<String> editCart(Map<String, String> requestMap);
}
