package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.ProductDao;
import com.daniilzverev.shopserver.dao.ShoppingCartDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.ShoppingCart;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.service.ShoppingCartService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.CartWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    UserDao userDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Autowired
    JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> addToCart(Map<String, String> requestMap) {
        if(checkAddOrEditCartMap(requestMap))
            try{
                log.info("User "+jwtFilter.getCurrentUser()+" Trying to add product to shopping cart:"+requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if(!Objects.isNull(user)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(requestMap.get("productId")));
                    if(optProduct.isPresent()){
                        ShoppingCart newItem= new ShoppingCart();
                        newItem.setProduct(optProduct.get());
                        newItem.setUser(user);
                        newItem.setQuantity(Integer.parseInt(requestMap.get("quantity")));
                        shoppingCartDao.save(newItem);

                        return Utils.getResponseEntity(Constants.ITEM_ADDED, HttpStatus.OK);
                    }else
                        return Utils.getResponseEntity(Constants.PRODUCT_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }
            }catch (Exception ex){
                log.error(ex.getLocalizedMessage());
            }

        else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeOfCart(Map<String, String> requestMap) {
        if(checkRemoveFromCartMap(requestMap))
            try{
                log.info("User "+jwtFilter.getCurrentUser()+" Trying to remove product to shopping cart:"+requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if(!Objects.isNull(user)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(requestMap.get("productId")));
                    if(optProduct.isPresent()){
                        ShoppingCart existingItem =
                                shoppingCartDao.findByProductAndUser( optProduct.get().getId(), user.getId());
                        if(!Objects.isNull(existingItem)) {
                            shoppingCartDao.delete(existingItem);
                            return Utils.getResponseEntity(Constants.REMOVED, HttpStatus.OK);
                        } else return Utils.getResponseEntity(Constants.ITEM_DONT_EXIST, HttpStatus.BAD_REQUEST);
                    }else
                        return Utils.getResponseEntity(Constants.PRODUCT_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }
            }catch (Exception ex){
                log.error(ex.getLocalizedMessage());
            }

        else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeAllOfCart() {
        try{
            log.info("User "+jwtFilter.getCurrentUser()+" Trying to clean shopping cart");
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)){
                shoppingCartDao.deleteAllByUserId(user.getId());
                return Utils.getResponseEntity(Constants.REMOVED, HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<CartWrapper>> getCart() {
        try{
            log.info("User "+jwtFilter.getCurrentUser()+" Trying to clean shopping cart");
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)){
                List<CartWrapper> result = productDao.findAllInShoppingCart(user.getId());
                return new ResponseEntity<List<CartWrapper>>(result,HttpStatus.OK);
            }
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editCart(Map<String, String> requestMap) {
        if(checkAddOrEditCartMap(requestMap))
            try{
                log.info("User "+jwtFilter.getCurrentUser()+" Trying to edit product to shopping cart:"+requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if(!Objects.isNull(user)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(requestMap.get("productId")));
                    if(optProduct.isPresent()){
                        ShoppingCart item=
                                shoppingCartDao.findByProductAndUser(optProduct.get().getId(),user.getId());
                        if(!Objects.isNull(item)){
                            item.setQuantity(Integer.parseInt(requestMap.get("quantity")));
                            shoppingCartDao.save(item);
                            return Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);
                        }else
                            return Utils.getResponseEntity(Constants.ITEM_DONT_EXIST, HttpStatus.BAD_REQUEST);
                    }else
                        return Utils.getResponseEntity(Constants.PRODUCT_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }
            }catch (Exception ex){
                log.error(ex.getLocalizedMessage());
            }

        else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean checkRemoveFromCartMap(Map<String, String> requestMap){
        if(requestMap.containsKey("productId"))
            try{
                Long.parseLong(requestMap.get("productId"));
                return true;
            }catch (NumberFormatException e){
                log.error("Bad Number format in Add To Cart Petition");
            }
        return false;
    }

    private boolean checkAddOrEditCartMap(Map<String, String> requestMap){
        if(requestMap.containsKey("productId")&&requestMap.containsKey("quantity"))
            try{
                Long.parseLong(requestMap.get("productId"));
                if(Integer.parseInt(requestMap.get("quantity"))>0)
                    return true;
            }catch (NumberFormatException e){
                log.error("Bad Number format in Add To Cart Petition");
            }
        return false;
    }
}
