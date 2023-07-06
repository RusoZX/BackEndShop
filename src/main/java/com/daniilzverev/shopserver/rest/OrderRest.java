package com.daniilzverev.shopserver.rest;

import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/order")
public interface OrderRest {
    @PostMapping(path="/create")
    ResponseEntity<String> createOrder(@RequestBody Map<String, String> requestMap);
    @GetMapping(path="/get{orderId}")
    ResponseEntity<String> getOrder(@PathVariable String orderId);
    @GetMapping(path="/getAll")
    ResponseEntity<List<OrderForClientWrapper>> getAllOrders();
    @PostMapping(path="/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);
}
