package com.daniilzverev.shopserver.rest;

import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/product")
public interface ProductRest {
    @PostMapping(path="/add")
    ResponseEntity<String> addProduct(@RequestBody Map<String, String> requestMap);
    @PostMapping(path="/edit")
    ResponseEntity<String> editProduct(@RequestBody Map<String, String> requestMap);
    @DeleteMapping(path="/remove")
    ResponseEntity<String> removeProduct(@RequestBody Map <String,String> requestMap);
    @GetMapping(path="/{productId}")
    ResponseEntity<Product> getProduct(@PathVariable Long productId);
    @GetMapping(path="/getBy{method}")
    ResponseEntity<List<ProductWrapper>> getProducts(@PathVariable String method,
                                                     @RequestParam(value = "limit") String limit,
                                                     @RequestParam(value = "search", required = false) String search);
    @GetMapping(path="/getCategories")
    ResponseEntity<List<String>> getCategories();
    @PostMapping(path="/changeCategories")
    ResponseEntity<String> changeCategories(@RequestBody Map<String, String> requestMap);
}
