package com.daniilzverev.shopserver.rest;

import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.wrapper.AddressWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path="/address/add")
    ResponseEntity<String> addAddress(@RequestBody Map<String, String> requestMap);

    @PostMapping(path="/address/edit")
    ResponseEntity<String> editAddress(@RequestBody Map<String, String> requestMap);

    @DeleteMapping(path="/address/remove")
    ResponseEntity<String> removeAddress(@RequestParam(value = "id") String id);

    @GetMapping(path="/address/getAll")
    ResponseEntity<List<AddressWrapper>> getAllAddress();

    @GetMapping(path="/address/getAllShops")
    ResponseEntity<List<AddressWrapper>> getAllShops();

    @GetMapping(path="/address/get{idAddress}")
    ResponseEntity<String> getAddress(@PathVariable String idAddress);

    @GetMapping(path="/check")
    ResponseEntity<String> checkToken(@RequestParam(value = "token") String token);
}
