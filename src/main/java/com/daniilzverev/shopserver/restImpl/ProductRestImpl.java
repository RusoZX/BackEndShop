package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.rest.ProductRest;
import com.daniilzverev.shopserver.service.ProductService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {
    @Autowired
    ProductService productService;

    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try{
            return productService.addProduct(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editProduct(Map<String, String> requestMap) {
        try{
            return productService.editProduct(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeProduct(Map<String, String> requestMap) {
        try{
            return productService.removeProduct(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Product> getProduct(Long productId) {
        try{
            return productService.getProduct(productId);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<Product>(new Product(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProducts(String method, String limit, String search) {
        try{
            return productService.getProducts(method,limit,search);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<String>> getCategories() {
        try{
            return productService.getCategories();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<String>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changeCategories(Map<String, String> requestMap) {
        try{
            return productService.changeCategories(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
