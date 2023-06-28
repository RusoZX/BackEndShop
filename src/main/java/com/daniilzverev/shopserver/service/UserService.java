package com.daniilzverev.shopserver.service;

import com.daniilzverev.shopserver.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);
    ResponseEntity<User> getUserData();
    ResponseEntity<String> updateProfile(Map<String, String> requestMap);
    ResponseEntity<String> changePwd(Map<String,String> requestMap);

}
