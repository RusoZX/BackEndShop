package com.daniilzverev.shopserver.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path="/cart")
public interface ShoppingCartRest {
    @PostMapping("/add")
    ResponseEntity<String> addToCart(@RequestBody Map<String, String> requestMap);
}
