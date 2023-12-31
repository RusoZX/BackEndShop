package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.ProductDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.service.ProductService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<String> removeProduct(String productId) {
        try {
            log.info("User " + jwtFilter.getCurrentUser() + " trying to remove Product:" + productId);
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)&&user.getRole().equals("employee"))
                if(checkRemoveProductMap(productId)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(productId));
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
                if(optProduct.isPresent()){
                    Product product= optProduct.get();
                    log.info("Response:"+product);
                    product.setImageData(getImage(product.getTitle()+product.getId()));
                    product.setType(getType(product.getTitle()+product.getId()));

                    return new ResponseEntity<Product>(product, HttpStatus.OK);
                }
                else {
                    log.info("Not found");
                    return new ResponseEntity<Product>(new Product(), HttpStatus.NOT_FOUND);
                }
            }else {
                log.info("Bad request");
                return new ResponseEntity<Product>(new Product(), HttpStatus.BAD_REQUEST);
            }
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
                List<ProductWrapper> res = new ArrayList<>();
                switch (method){
                    case "None":
                        res = productDao.findAllByNone(requestLimit);
                        break;
                    case "Title":
                        if(!Objects.isNull(search))
                            res =productDao.findAllByTitle(search, requestLimit);
                        break;
                    case "Category":
                        if(!Objects.isNull(search))
                            res = productDao.findAllByCategory(search, requestLimit);
                        break;
                    case "PriceAsc":
                        res =productDao.findAllByPriceAsc(requestLimit);
                        break;
                    case "PriceDesc":
                        res = productDao.findAllByPriceDesc(requestLimit);
                        break;
                    case "Brand":
                        if(!Objects.isNull(search))
                            res =productDao.findAllByBrand(search, requestLimit);
                        break;
                    case "Color":
                        if(!Objects.isNull(search))
                            res = productDao.findAllByColor(search, requestLimit);
                        break;
                    case "BestSellers":
                        res = productDao.findAllByBestSellers(requestLimit);
                        break;
                    default:
                        //If it got here it means that search, method or limit had bad format
                        log.info("Bad Request");
                        return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
                }
                log.info("Result:"+res);
                return new ResponseEntity<List<ProductWrapper>>(addImages(res), HttpStatus.OK);

            }

        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
            return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<String>> getCategories() {
        try{
            List<String> res = productDao.findAllCategories();
            log.info("Result:"+res);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @Override
    public ResponseEntity<String> updateImg(String productId, MultipartFile img) {
        try {
            log.info("User " + jwtFilter.getCurrentUser() + " trying to add image to Product:" + productId);
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)&&user.getRole().equals("employee"))
                if(checkRemoveProductMap(productId)){
                    Optional<Product> optProduct = productDao.findById(Long.parseLong(productId));
                    if(optProduct.isPresent()){
                        Product product = optProduct.get();
                        if(!createImage(product.getTitle().concat(product.getId().toString()), img).isEmpty())
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
    private boolean checkRemoveProductMap(String productId){
        if(!productId.isEmpty()){
            try{
                Long.parseLong(productId);
                return true;
            }catch (NumberFormatException e){
                log.error("Bad number format");
            }
        }
        return false;
    }
    private String createImage(String name, MultipartFile img) throws IOException{

        String contentType = img.getContentType();
        String fileExtension = contentType.substring(contentType.lastIndexOf("/") + 1);
        File newFile= new File("C:\\images\\"+name + "." + fileExtension);
        File[] files = newFile.getParentFile().listFiles((dir, fileName) -> fileName.startsWith(name + ".") && !fileName.equals(newFile.getName()));
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        img.transferTo(newFile);
        return newFile.getPath();
    }
    private byte[] getImage(String name) throws Exception{
        File[] files = new File("C:\\images\\").listFiles((dir, fileName) -> fileName.startsWith(name + ".") );
        if(files.length!=0) {
            Path path = Paths.get(files[0].getAbsolutePath());
            return Files.readAllBytes(path);
        }
        return null;


    }
    private String getType(String name){
        File[] files = new File("C:\\images\\").listFiles((dir, fileName) -> fileName.startsWith(name + ".") );
        if(files.length!=0)
            return files[0].getName().substring(files[0].getName().lastIndexOf(".") + 1);

        else
            return null;

    }
    private List<ProductWrapper> addImages(List<ProductWrapper> products) throws Exception{

        for(ProductWrapper product: products){
            byte[] img = getImage(product.getTitle()+product.getId());
            if(!Objects.isNull(img)) {
                product.setImageData(img);
                product.setType(getType(product.getTitle() + product.getId()));
            }
        }
        return products;
    }
}
