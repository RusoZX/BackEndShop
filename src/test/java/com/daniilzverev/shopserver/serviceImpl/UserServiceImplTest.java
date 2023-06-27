package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.CustomerUsersDetailsService;
import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserDao userDao;

    @Mock
    private CustomerUsersDetailsService customerUserDetailsService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

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
        assertEquals("{\"message\":\""+Constants.REGISTERED+"\"}" , response.getBody());
    }


    @Test
    public void signUpUserExists() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","2001-09-11");
        requestMap.put("email","example1@example.com");
        requestMap.put("pwd","someEncryptedData");

        when(userDao.findByEmail(anyString())).thenReturn(new User());

        ResponseEntity<String> response = userService.signUp(requestMap);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.USER_EXISTS+"\"}" , response.getBody());

    }

    @Test
    public void signUpInvalidData() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","no format");
        requestMap.put("email","example22@example.com");
        requestMap.put("pwd","someEncryptedData");

        ResponseEntity<String> response = userService.signUp(requestMap);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}" , response.getBody());
    }

    @Test
    public void loginSuccess() {
        Map<String, String> requestMap = new HashMap<>();

        String email = "example1@example.com";
        String pwd = "someEncryptedData";
        requestMap.put("email",email);
        requestMap.put("pwd",pwd);

        User user = new User();
        user.setEmail(email);
        user.setPwd(email);

        //We mock the actions of the function
        when(customerUserDetailsService.getUserDetail()).thenReturn(user);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));

        when(jwtUtil.generateToken(email, pwd)).thenReturn(any());

        ResponseEntity<String> response = userService.login(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void loginBadCredentials() {
        Map<String, String> requestMap = new HashMap<>();

        String email = "example@example.com";
        String pwd = "someEncryptedData";
        requestMap.put("email",email);
        requestMap.put("pwd",pwd);

        User user = new User();
        user.setEmail(email);
        user.setPwd(email);

        //We mock the actions of the function
        when(customerUserDetailsService.getUserDetail()).thenReturn(user);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException(""));

        when(jwtUtil.generateToken(email, pwd)).thenReturn(any());

        ResponseEntity<String> response = userService.login(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.BAD_CREDENTIALS+"\"}" , response.getBody());
    }
    @Test
    public void loginBadData() {
        Map<String, String> requestMap = new HashMap<>();


        String pwd = "someEncryptedData";

        ResponseEntity<String> response = userService.login(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}" , response.getBody());
    }


}