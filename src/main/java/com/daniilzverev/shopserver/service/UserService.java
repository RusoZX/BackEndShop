package com.daniilzverev.shopserver.service;

import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.wrapper.AddressWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);
    ResponseEntity<User> getUserData();
    ResponseEntity<String> updateProfile(Map<String, String> requestMap);
    ResponseEntity<String> changePwd(Map<String,String> requestMap);
    ResponseEntity<List<User>> getUsers();
    ResponseEntity<String> addAddress(Map<String, String> requestMap);
    ResponseEntity<String> editAddress(Map<String, String> requestMap);
    ResponseEntity<String> removeAddress(Map<String, String> requestMap);
    ResponseEntity<List<AddressWrapper>> getAllAddress();
    ResponseEntity<String> getAddress(String idAddress);
    ResponseEntity<String> checkToken(String token);
}
