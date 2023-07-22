package com.daniilzverev.shopserver.service;

import com.daniilzverev.shopserver.wrapper.FullOrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.GoodsWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ResponseEntity<String> createOrder(Map<String, String> requestMap);
    ResponseEntity<FullOrderForClientWrapper> getOrder(String orderId);
    ResponseEntity<List<OrderForClientWrapper>> getAllOrders();
    ResponseEntity<String> updateStatus(Map<String, String> requestMap);
    ResponseEntity<List<OrderForEmployeeWrapper>> getAllOrdersEmployee(String mode);
    ResponseEntity<List<GoodsWrapper>> getAllGoods(String orderId);
}
