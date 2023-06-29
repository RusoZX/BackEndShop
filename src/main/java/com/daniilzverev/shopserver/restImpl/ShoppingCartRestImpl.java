package com.daniilzverev.shopserver.restImpl;


import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.rest.ShoppingCartRest;
import com.daniilzverev.shopserver.service.ShoppingCartService;
import com.daniilzverev.shopserver.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ShoppingCartRestImpl implements ShoppingCartRest {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Override
    public ResponseEntity<String> addToCart(Map<String, String> requestMap) {
        try{
            return shoppingCartService.addToCart(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeOfCart(Map<String, String> requestMap) {
        try{
            return shoppingCartService.removeOfCart(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
