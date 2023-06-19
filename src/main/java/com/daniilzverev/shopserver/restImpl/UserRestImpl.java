package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.rest.UserRest;
import com.daniilzverev.shopserver.service.UserService;
import com.daniilzverev.shopserver.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
