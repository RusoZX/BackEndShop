package com.daniilzverev.shopserver.utils;

import com.daniilzverev.shopserver.constants.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Map;

public class Utils {
    static PasswordEncoder pwdEncoder = new BCryptPasswordEncoder(10, new SecureRandom(Constants.SECRET.getBytes()));
    private Utils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
    }
    public static String requestMapToJson(Map<String, String> requestMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestMap);
    }
    public static boolean matches(String toTry, String actual){
        return pwdEncoder.matches(toTry, actual);
    }
    public static String encode(String toEncode){
        return pwdEncoder.encode(toEncode);
    }
}
