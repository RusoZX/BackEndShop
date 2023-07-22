package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.*;
import com.daniilzverev.shopserver.entity.*;
import com.daniilzverev.shopserver.service.OrderService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.FullOrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.GoodsWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    UserDao userDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    AddressDao addressDao;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> createOrder(Map<String, String> requestMap) {
        try{
            if(checkCreateOrderMap(requestMap)){
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to create order: "+requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)) {
                    List<Goods> goods = getGoods(requestMap.get("goods"));
                    if(!Objects.isNull(goods)){
                        Order order = new Order();
                        if (requestMap.get("deliveryMethod").equals("delivery")) {
                            Optional<Address> optAddress = addressDao.findById(Long.parseLong(requestMap.get("addressId")));
                            if(optAddress.isPresent()) {
                                order.setAddress(optAddress.get());
                                order.setUser(user);
                                order.setDeliveryMethod(requestMap.get("deliveryMethod"));
                                order.setPaymentMethod(requestMap.get("paymentMethod"));
                                order.setPaymentStatus(false);
                                order.setOrderStatus("pending");
                                orderDao.save(order);
                                for(Goods item :goods){
                                    item.setOrder(order);
                                    Optional<Product> optProd= productDao.findById(item.getProduct().getId());
                                    if(optProd.isPresent()){
                                        Product product = optProd.get();
                                        if(item.getQuantity()<=product.getStock()&&item.getQuantity()>0){
                                            product.setStock(product.getStock()-item.getQuantity());
                                            productDao.save(product);
                                        }else
                                            return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                                        goodsDao.save(item);
                                    }else
                                        return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);

                                }
                                return Utils.getResponseEntity(Constants.ORDER_CREATED, HttpStatus.OK);
                            }
                            else
                                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                        }

                    }else
                        return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<FullOrderForClientWrapper> getOrder(String orderId) {
        try{
            if(checkOrderId(orderId)){
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to get order: "+orderId);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)){
                    Optional<Order> order = orderDao.findById(Long.parseLong(orderId));
                    if(order.isPresent()){
                        if(user.equals(order.get().getUser())||user.getRole().equals("employee")){
                            return new ResponseEntity<FullOrderForClientWrapper>
                                    (orderDao.findFullByOrderId(Long.parseLong(orderId)), HttpStatus.OK);
                        }else
                            return new ResponseEntity<FullOrderForClientWrapper>(new FullOrderForClientWrapper(), HttpStatus.UNAUTHORIZED);
                    }else
                        return new ResponseEntity<FullOrderForClientWrapper>(new FullOrderForClientWrapper(), HttpStatus.NOT_FOUND);
                }
            } else
                return new ResponseEntity<FullOrderForClientWrapper>(new FullOrderForClientWrapper(), HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return new ResponseEntity<FullOrderForClientWrapper>(new FullOrderForClientWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<OrderForClientWrapper>> getAllOrders() {
        try{
            log.info("User " + jwtFilter.getCurrentUser() + " Trying to get orders: ");
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!Objects.isNull(user)){
                return new ResponseEntity<>(orderDao.findAllByUserId(user.getId()), HttpStatus.OK);
            }
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            if(checkUpdateStatusMap(requestMap)) {
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to get orders: ");
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)&&user.getRole().equals("employee")){
                    Optional<Order> optOrder = orderDao.findById(Long.parseLong(requestMap.get("orderId")));
                    if(optOrder.isPresent()){
                        Order order = optOrder.get();
                        if(requestMap.get("update").equals("payment"))
                            order.setPaymentStatus(requestMap.get("updateData").equals("true"));
                        if(requestMap.get("update").equals("status"))
                            order.setOrderStatus(requestMap.get("updateData"));
                        orderDao.save(order);
                        return  Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);
                    }else
                        return  Utils.getResponseEntity(Constants.ORDER_DONT_EXIST, HttpStatus.NOT_FOUND);
                }else
                    return Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
            }else
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);

        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<OrderForEmployeeWrapper>> getAllOrdersEmployee(String mode,String search) {
        try{
            log.info("User " + jwtFilter.getCurrentUser() + " Trying to get orders: "+mode);
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!Objects.isNull(user)&&user.getRole().equals("employee")){
                if(Objects.isNull(mode))
                    return new ResponseEntity<>(orderDao.findAllNone(), HttpStatus.OK);
                if(mode.equals("week"))
                    return new ResponseEntity<>(orderDao.findAllTimeInterval(giveWeekAgo(), LocalDate.now()), HttpStatus.OK);
                if(mode.equals("month"))
                    return new ResponseEntity<>(orderDao.findAllTimeInterval(giveMonthAgo(), LocalDate.now()), HttpStatus.OK);
                if(mode.equals("pending"))
                    return new ResponseEntity<>(orderDao.findAllPending(), HttpStatus.OK);
                if(mode.equals("byUser"))
                    return new ResponseEntity<>(orderDao.findAllByUser(search), HttpStatus.OK);


                //If it got here it means that there was a bad format
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }else
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<GoodsWrapper>> getAllGoods(String orderId) {
        try{
            if(checkOrderId(orderId)){
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to get goods of the order: "+orderId);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if(!Objects.isNull(user)){
                    Optional<Order> optOrder = orderDao.findById(Long.parseLong(orderId));
                    if(optOrder.isPresent()){
                        return new ResponseEntity<List<GoodsWrapper>>
                                (goodsDao.findAllGoods(optOrder.get().getId()), HttpStatus.OK);
                    }else
                        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean checkOrderId(String orderId){
        if(!Objects.isNull(orderId)){
            try{
                Long.parseLong(orderId);
                return true;
            }catch (NumberFormatException e){
                log.error("order id bad format");
            }
        }
        return false;
    }
    private boolean checkCreateOrderMap(Map<String, String> requestMap){
        if(requestMap.containsKey("deliveryMethod")
                &&requestMap.containsKey("paymentMethod")&&requestMap.containsKey("goods")){
            try {
                if (requestMap.get("deliveryMethod").equals("delivery")) {
                    if (requestMap.containsKey("addressId")) {
                        Long.parseLong(requestMap.get("addressId"));
                        return true;
                    }
                    return false;
                }
                return true;
            }catch (NumberFormatException e){
                log.error(e.getLocalizedMessage());
            }
        }
        return false;
    }
    private boolean checkUpdateStatusMap(Map<String, String> requestMap){
        if(requestMap.containsKey("orderId")&&requestMap.containsKey("update")&&requestMap.containsKey("updateData")
        &&(requestMap.get("update").equals("payment")||requestMap.get("update").equals("status"))){
            try {

                Long.parseLong(requestMap.get("orderId"));
                return true;
            }catch (NumberFormatException e){
                log.error(e.getLocalizedMessage());
            }
        }
        return false;
    }
    private String createJsonResponseForOrder(Order order){
        List<Goods> goods = goodsDao.findAllByOrderId(order.getId());
        String result = order.toJson();
        result =result.substring(0, result.length()-1).concat(",\"goods\":[");
        for(Goods item : goods)
            result = result.concat(item.toJson()).concat(",");

        return result.substring(0, result.length()-1).concat("]}");
    }
    //Here if something is not as expected it will return a null, in wich case we will know that something went
    //wrong and send a BAD_REQUEST response to the client
    private List<Goods> getGoods(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Goods> result = new ArrayList<>();
            // Parse the JSON array
            JsonNode jsonNode = objectMapper.readTree(json);

            for (JsonNode element : jsonNode) {
                Goods item = new Goods();
                Optional<Product> optProduct = productDao.findById(element.get("productId").asLong());
                if(optProduct.isPresent()){
                    if(optProduct.get().getStock()>element.get("quantity").asInt()){
                        item.setProduct(optProduct.get());
                        item.setQuantity(element.get("quantity").asInt());
                        result.add(item);
                    }else
                        return null;
                } else
                    return null;
            }
            return result;

        }catch (InvalidFormatException e) {
            log.error("Bad Number format in goods "+e.getLocalizedMessage());
        }
        catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
        return null;
    }
    private LocalDate giveWeekAgo(){
        LocalDate now = LocalDate.now();
        return  now.minusWeeks(1);
    }
    private LocalDate giveMonthAgo(){
        LocalDate now = LocalDate.now();
        return  now.minusMonths(1);
    }
}
