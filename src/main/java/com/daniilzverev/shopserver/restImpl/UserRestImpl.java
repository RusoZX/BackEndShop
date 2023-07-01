package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.rest.UserRest;
import com.daniilzverev.shopserver.service.UserService;
import com.daniilzverev.shopserver.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            return userService.signUp(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<User> getUserData() {
        try{
            return userService.getUserData();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<User>(new User(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> updateProfile(Map<String, String> requestMap) {
        try{
            return userService.updateProfile(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePwd(Map<String, String> requestMap) {
        try{
            return userService.changePwd(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        try{
            return userService.getUsers();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<User>>(new ArrayList<User>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
