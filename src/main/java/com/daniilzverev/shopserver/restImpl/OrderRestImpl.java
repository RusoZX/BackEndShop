package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.rest.OrderRest;
import com.daniilzverev.shopserver.serviceImpl.OrderServiceImpl;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class OrderRestImpl implements OrderRest {
    @Autowired
    OrderServiceImpl orderService;

    @Override
    public ResponseEntity<String> createOrder(Map<String, String> requestMap) {
        try{
            return orderService.createOrder(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> getOrder(String orderId) {
        try{
            return orderService.getOrder(orderId);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<OrderForClientWrapper>> getAllOrders() {
        try{
            return orderService.getAllOrders();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<OrderForClientWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            return orderService.updateStatus(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
