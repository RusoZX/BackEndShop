package com.daniilzverev.shopserver.rest;

import com.daniilzverev.shopserver.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/user")
public interface UserRest {

    @PostMapping(path="/signup")
    ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap);

    @PostMapping(path="/login")
    ResponseEntity<String> login(@RequestBody Map<String, String> requestMap);

    @GetMapping(path="/profile")
    ResponseEntity<User> getUserData();

    @PostMapping(path="/profile/update")
    ResponseEntity<String> updateProfile(@RequestBody Map<String, String> requestMap);

    @PostMapping(path="/profile/changepwd")
    ResponseEntity<String> changePwd(@RequestBody Map<String, String> requestMap);

    @GetMapping(path="/users")
    ResponseEntity<List<User>> getUsers();
}
