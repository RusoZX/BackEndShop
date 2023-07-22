package com.daniilzverev.shopserver.rest;

import com.daniilzverev.shopserver.wrapper.FullOrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.GoodsWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/order")
public interface OrderRest {
    @PostMapping(path="/create")
    ResponseEntity<String> createOrder(@RequestBody Map<String, String> requestMap);
    @GetMapping(path="/get{orderId}")
    ResponseEntity<FullOrderForClientWrapper> getOrder(@PathVariable String orderId);
    @GetMapping(path="/getAll")
    ResponseEntity<List<OrderForClientWrapper>> getAllOrders();
    @PostMapping(path="/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);

    @GetMapping(path="/getAllOrders")
    ResponseEntity<List<OrderForEmployeeWrapper>> getOrdersForEmployee(@RequestParam(value = "search", required = false) String mode);

    @GetMapping(path="/getGoods{orderId}")
    ResponseEntity<List<GoodsWrapper>> getGoods(@PathVariable String orderId);
}
