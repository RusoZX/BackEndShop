package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.rest.ProductRest;
import com.daniilzverev.shopserver.service.ProductService;
import com.daniilzverev.shopserver.utils.Utils;
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

    /*@Override
    public ResponseEntity<List<Product>> getProducts(String method, String param1) {
        try{
            return productService.getProducts(method,param1);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Product>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
