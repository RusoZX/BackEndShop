package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.ProductDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Goods;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.service.ProductService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    UserDao userDao;
    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try {
            log.info("User " + jwtFilter.getCurrentUser() + " trying to add Product:" + requestMap);
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)&&user.getRole().equals("employee"))
                if(checkAddProductMap(requestMap)){
                    Product product = new Product();
                    product.setTitle(requestMap.get("title"));
                    product.setPrice(Float.parseFloat(requestMap.get("price")));
                    product.setCategory(requestMap.get("category"));
                    product.setBrand(requestMap.get("brand"));
                    product.setColor(requestMap.get("color"));
                    product.setWeight(Float.parseFloat(requestMap.get("weight")));
                    product.setVolume(Float.parseFloat(requestMap.get("volume")));
                    product.setStock(Integer.parseInt(requestMap.get("stock")));
                    product.setTotalSold(0);
                    productDao.save(product);
                    return Utils.getResponseEntity(Constants.PRODUCT_ADDED, HttpStatus.OK);
                }else
                    return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            else
                return Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);

        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editProduct(Map<String, String> requestMap) {
        try {
            log.info("User " + jwtFilter.getCurrentUser() + " trying to edit Product:" + requestMap);
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)&&user.getRole().equals("employee"))
                if(checkEditProductMap(requestMap)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(requestMap.get("productId")));
                    if(optProduct.isPresent()){
                        Product product = optProduct.get();
                        if(requestMap.containsKey("title"))
                            product.setTitle(requestMap.get("title"));
                        if(requestMap.containsKey("price"))
                            product.setPrice(Float.parseFloat(requestMap.get("price")));
                        if(requestMap.containsKey("category"))
                            product.setCategory(requestMap.get("category"));
                        if(requestMap.containsKey("brand"))
                            product.setBrand(requestMap.get("brand"));
                        if(requestMap.containsKey("color"))
                            product.setColor(requestMap.get("color"));
                        if(requestMap.containsKey("weight"))
                            product.setWeight(Float.parseFloat(requestMap.get("weight")));
                        if(requestMap.containsKey("volume"))
                            product.setVolume(Float.parseFloat(requestMap.get("volume")));
                        if(requestMap.containsKey("stock"))
                            product.setStock(Integer.parseInt(requestMap.get("stock")));
                        productDao.save(product);
                        return Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);
                    }else
                        return Utils.getResponseEntity(Constants.PRODUCT_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }else
                    return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            else
                return Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);

        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeProduct(Map<String, String> requestMap) {
        try {
            log.info("User " + jwtFilter.getCurrentUser() + " trying to remove Product:" + requestMap);
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)&&user.getRole().equals("employee"))
                if(checkRemoveProductMap(requestMap)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(requestMap.get("productId")));
                    if(optProduct.isPresent()){
                        productDao.delete(optProduct.get());
                        return Utils.getResponseEntity(Constants.REMOVED, HttpStatus.OK);
                    }else
                        return Utils.getResponseEntity(Constants.PRODUCT_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }else
                    return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            else
                return Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);

        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Product> getProduct(Long productId) {
        try{
            log.info("Giving product info of id "+productId);
            if(!Objects.isNull(productId)){
                Optional<Product> optProduct = productDao.findById(productId);
                if(optProduct.isPresent())
                    return new ResponseEntity<Product>(optProduct.get(), HttpStatus.OK);
                else
                    return new ResponseEntity<Product>(new Product(), HttpStatus.NOT_FOUND);
            }else
                return new ResponseEntity<Product>(new Product(), HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
            return new ResponseEntity<Product>(new Product(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProducts(String method, String limit, String search) {
        try{
            log.info("Giving products filter by "+method+", limit "+limit+",search "+search);
            if(checkGetProductsRequest(method,limit)){
                Pageable requestLimit = PageRequest.of(0,Integer.parseInt(limit));
                switch (method){
                    case "None":
                        return new ResponseEntity<List<ProductWrapper>>(
                                productDao.findAllByNone(requestLimit),
                                HttpStatus.OK);
                    case "Title":
                        if(!Objects.isNull(search))
                            return new ResponseEntity<List<ProductWrapper>>(
                                    productDao.findAllByTitle(search, requestLimit),
                                    HttpStatus.OK);
                        break;
                    case "Category":
                        if(!Objects.isNull(search))
                            return new ResponseEntity<List<ProductWrapper>>(
                                    productDao.findAllByCategory(search, requestLimit),
                                    HttpStatus.OK);
                        break;
                    case "PriceAsc":
                        return new ResponseEntity<List<ProductWrapper>>(
                                productDao.findAllByPriceAsc(requestLimit),
                                HttpStatus.OK);
                    case "PriceDesc":
                        return new ResponseEntity<List<ProductWrapper>>(
                                productDao.findAllByPriceDesc(requestLimit),
                                HttpStatus.OK);
                    case "Brand":
                        if(!Objects.isNull(search))
                            return new ResponseEntity<List<ProductWrapper>>(
                                    productDao.findAllByBrand(search, requestLimit),
                                    HttpStatus.OK);
                        break;
                    case "Color":
                        if(!Objects.isNull(search))
                            return new ResponseEntity<List<ProductWrapper>>(
                                    productDao.findAllByColor(search, requestLimit),
                                    HttpStatus.OK);
                        break;
                    case "BestSellers":
                        return new ResponseEntity<List<ProductWrapper>>(
                                productDao.findAllByBestSellers( requestLimit),
                                HttpStatus.OK);
                }

            }
            //If it got here it means that search, method or limit had bad format
            return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
            return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<String>> getCategories() {
        try{
            return new ResponseEntity<>(productDao.findAllCategories(), HttpStatus.OK);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @Override
    public ResponseEntity<String> changeCategories(Map<String, String> requestMap) {
        try {
            if(checkChangeCategoriesMap(requestMap)) {
                log.info("User " + jwtFilter.getCurrentUser() + " trying to change products categories:" + requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user) && user.getRole().equals("employee")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(requestMap.get("productList"));
                    for (JsonNode element : jsonNode) {
                        Long id = Long.parseLong(element.get("id").textValue());
                        Optional<Product> optProduct= productDao.findById(Long.parseLong(element.get("id").textValue()));
                        if(optProduct.isPresent()){
                            Product product = optProduct.get();
                            product.setCategory(requestMap.get("newCategory"));
                            productDao.save(product);
                        }else
                            return Utils.getResponseEntity(Constants.PRODUCT_DONT_EXIST, HttpStatus.NOT_FOUND);
                    }
                    return Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);
                } else
                    return Utils.getResponseEntity(Constants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
            }else
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (NumberFormatException e) {
            log.error("Bad Number format in product Id "+e.getLocalizedMessage());
            return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean checkGetProductsRequest(String method, String limit){
        if(!Objects.isNull(method)&&!Objects.isNull(limit))
            try{
                Integer limitInt = Integer.parseInt(limit);
                if(limitInt>0)
                    return true;
            }catch (NumberFormatException e){
                log.error("Number format exception with limit");
            }
        return false;
    }

    private boolean checkAddProductMap(Map<String, String> requestMap){
        if(requestMap.containsKey("title")&&requestMap.containsKey("price")&&requestMap.containsKey("category")&&
                requestMap.containsKey("brand")&&requestMap.containsKey("color")&&requestMap.containsKey("weight")&&
                requestMap.containsKey("volume")&&requestMap.containsKey("stock")){
            try{

                if(Float.parseFloat(requestMap.get("price"))>0&&
                        Float.parseFloat(requestMap.get("weight"))>0&&
                        Float.parseFloat(requestMap.get("volume"))>0&&
                        Integer.parseInt(requestMap.get("stock"))>0)
                    return true;
            }catch (NumberFormatException e){
                log.error("Bad number format");
            }
        }
        return false;
    }
    private boolean checkEditProductMap(Map<String, String> requestMap){
        if(requestMap.containsKey("productId")&&(requestMap.containsKey("title")||requestMap.containsKey("price")||requestMap.containsKey("category")||
                requestMap.containsKey("brand")||requestMap.containsKey("color")||requestMap.containsKey("weight")||
                requestMap.containsKey("volume")||requestMap.containsKey("stock"))){
            try{
                Long.parseLong(requestMap.get("productId"));
                if(requestMap.containsKey("price")&&Float.parseFloat(requestMap.get("price"))<=0)
                    return false;
                if(requestMap.containsKey("weight")&&Float.parseFloat(requestMap.get("weight"))<=0)
                    return false;
                if(requestMap.containsKey("volume")&&Float.parseFloat(requestMap.get("volume"))<=0)
                    return false;
                if(requestMap.containsKey("stock")&&Integer.parseInt(requestMap.get("stock"))<=0)
                    return false;
                return true;
            }catch (NumberFormatException e){
                log.error("Bad number format");
            }
        }
        return false;
    }
    private boolean checkRemoveProductMap(Map<String, String> requestMap){
        if(requestMap.containsKey("productId")){
            try{
                Long.parseLong(requestMap.get("productId"));
                return true;
            }catch (NumberFormatException e){
                log.error("Bad number format");
            }
        }
        return false;
    }
    private boolean checkChangeCategoriesMap(Map<String, String> requestMap){
        return requestMap.containsKey("productList")&&requestMap.containsKey("newCategory");
    }
}
