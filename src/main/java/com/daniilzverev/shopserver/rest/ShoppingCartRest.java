package com.daniilzverev.shopserver.rest;


import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.wrapper.CartWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/cart")
public interface ShoppingCartRest {
    @PostMapping("/add")
    ResponseEntity<String> addToCart(@RequestBody Map<String, String> requestMap);
    @DeleteMapping("/remove")
    ResponseEntity<String> removeOfCart(@RequestParam String id);
    @DeleteMapping("/removeAll")
    ResponseEntity<String> removeAllOfCart();
    @GetMapping("/get")
    ResponseEntity<List<CartWrapper>> getAll();
    @PostMapping("/edit")
    ResponseEntity<String> editCart(@RequestBody Map<String, String> requestMap);
}
