package com.daniilzverev.shopserver.service;

import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addProduct(Map<String, String> requestMap);
    ResponseEntity<String> editProduct(Map<String, String> requestMap);
    ResponseEntity<String> removeProduct(String productId);
    ResponseEntity<Product> getProduct(Long productId);
    ResponseEntity<List<ProductWrapper>> getProducts(String method, String limit, String search);
    ResponseEntity<List<String>> getCategories();
}
