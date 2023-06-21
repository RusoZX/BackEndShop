package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUpSuccess() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","2001-09-11");
        requestMap.put("email","example1@example.com");
        requestMap.put("pwd","someEncryptedData");

        when(userDao.findByEmail(anyString())).thenReturn(null);
        when(userDao.save(any(User.class))).thenReturn(new User());

        ResponseEntity<String> response = userService.signUp(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void userExists() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","2001-09-11");
        requestMap.put("email","example1@example.com");
        requestMap.put("pwd","someEncryptedData");

        when(userDao.findByEmail(anyString())).thenReturn(new User());

        ResponseEntity<String> response = userService.signUp(requestMap);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void invalidData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","no format");
        requestMap.put("email","example22@example.com");
        requestMap.put("pwd","someEncryptedData");

        ResponseEntity<String> response = userService.signUp(requestMap);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }



}