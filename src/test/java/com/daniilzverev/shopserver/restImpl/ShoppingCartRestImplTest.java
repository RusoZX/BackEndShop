package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.daniilzverev.shopserver.utils.Utils.requestMapToJson;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/insert_test_data_shopping_cart.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_shopping_cart.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartRestImplTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void addToCartWithCorrectData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("quantity","3");

        MvcResult result = mockMvc.perform(post("/cart/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.ITEM_ADDED+"\"}", response);
    }
    @Test
    void addToCartWithBadData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();

        MvcResult result = mockMvc.perform(post("/cart/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addToCartWithBadID() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");
        requestMap.put("quantity","3");

        MvcResult result = mockMvc.perform(post("/cart/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addToCartWithBadQuantity() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("quantity","badFormat");

        MvcResult result = mockMvc.perform(post("/cart/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addToCartButProductDontExists() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","0");
        requestMap.put("quantity","10");

        MvcResult result = mockMvc.perform(post("/cart/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.PRODUCT_DONT_EXIST+"\"}", response);
    }
    @Test
    void addToCartWithoutAuth() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","0");
        requestMap.put("quantity","badFormat");

        mockMvc.perform(post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}