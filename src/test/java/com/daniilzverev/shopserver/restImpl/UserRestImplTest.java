package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/insert_test_data_user_rest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_user_rest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

class UserRestImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void signUpWithCorrectData() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","2001-09-11");
        requestMap.put("email","example@example.com");
        requestMap.put("pwd","someEncryptedData");

        MvcResult result = mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.REGISTERED+"\"}", response);
    }
    @Test
    public void userAlreadyExists() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","2001-09-11");
        requestMap.put("email","example1@example.com");
        requestMap.put("pwd","someEncryptedData");

        MvcResult result = mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.USER_EXISTS+"\"}", response);
    }
    @Test
    public void signUpWithIncorrectDate() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");
        requestMap.put("birthdate","no format");
        requestMap.put("email","example22@example.com");
        requestMap.put("pwd","someEncryptedData");


        MvcResult result = mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    public void signUpWithIncorrectData() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","test");
        requestMap.put("surname","test1");


        MvcResult result = mockMvc.perform(post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}", response);
    }

    @Test
    public void logInWithCorrectData() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email","example1@example.com");
        requestMap.put("pwd","someEncryptedData");

        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    public void logInWithBadCredentials() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email","example@example.com");
        requestMap.put("pwd","someEncryptedData");

        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.BAD_CREDENTIALS+"\"}", response);
    }
    @Test
    public void logInWithBadData() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email","example1@example.com");

        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    public void getProfile() throws Exception{

        MvcResult result = mockMvc.perform(get("/user/profile")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"id\":null,\"name\":\"test\",\"surname\":\"test1\",\"birthDate\":\"2001-09-11\"," +
                "\"email\":\"example1@example.com\",\"pwd\":null,\"role\":null,\"shoppingCart\":[]}"
                , response);
    }
    @Test
    public void getProfileNoAuth() throws Exception{

        mockMvc.perform(get("/user/profile")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("nobody","someEncryptedData")))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private String requestMapToJson(Map<String, String> requestMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestMap);
    }

}