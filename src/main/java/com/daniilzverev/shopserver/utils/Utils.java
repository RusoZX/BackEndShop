package com.daniilzverev.shopserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Utils {
    private Utils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
    }
    public static String requestMapToJson(Map<String, String> requestMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestMap);
    }
}
