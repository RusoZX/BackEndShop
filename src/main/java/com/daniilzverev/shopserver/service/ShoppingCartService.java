package com.daniilzverev.shopserver.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ShoppingCartService {
    ResponseEntity<String> addToCart(Map<String, String> requestMap);
    ResponseEntity<String> removeOfCart(Map<String, String> requestMap);
}
