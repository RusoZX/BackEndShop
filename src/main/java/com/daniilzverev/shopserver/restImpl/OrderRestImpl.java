package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.rest.OrderRest;
import com.daniilzverev.shopserver.serviceImpl.OrderServiceImpl;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.FullOrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.GoodsWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper;
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
    public ResponseEntity<FullOrderForClientWrapper> getOrder(String orderId) {
        try{
            return orderService.getOrder(orderId);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<FullOrderForClientWrapper>(new FullOrderForClientWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @Override
    public ResponseEntity<List<OrderForEmployeeWrapper>> getOrdersForEmployee(String mode,String search) {
        try{
            return orderService.getAllOrdersEmployee(mode, search);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<OrderForEmployeeWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getGoods(String orderId) {
        try{
            return orderService.getAllGoods(orderId);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<GoodsWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
