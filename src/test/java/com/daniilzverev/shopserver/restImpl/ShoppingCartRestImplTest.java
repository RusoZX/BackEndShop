package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
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
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
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
        mockMvc.perform(post("/cart/add"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void removeFromCartWithBadData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();

        MvcResult result = mockMvc.perform(delete("/cart/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA +"\"}", response);
    }
    @Test
    void removeFromCartWithBadFormat() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");
        MvcResult result = mockMvc.perform(delete("/cart/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA +"\"}", response);
    }
    @Test
    void removeFromCartWithBadProduct() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");
        MvcResult result = mockMvc.perform(delete("/cart/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA +"\"}", response);
    }
    @Test
    void removeFromCartWithoutAuth() throws Exception {
        mockMvc.perform(delete("/cart/remove"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void removeFromCartWithCorrectData() throws Exception {

        MvcResult result = mockMvc.perform(delete("/cart/remove?id=-1")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                )
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.REMOVED +"\"}", response);
    }
    @Test
    void removeAllFromCartWithoutAuth() throws Exception {
        mockMvc.perform(delete("/cart/removeAll"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void removeAllFromCartWithCorrectData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");

        MvcResult result = mockMvc.perform(delete("/cart/removeAll")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.REMOVED +"\"}", response);
    }
    @Test
    void getCartWithCorrectData() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart/get")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        //full json
        assertEquals("[{\"id\":-2,\"productId\":-2,\"title\":\"test4\",\"price\":10.0,\"quantity\":4}" +
                ",{\"id\":-1,\"productId\":-1,\"title\":\"test3\",\"price\":10.0,\"quantity\":3}]", response);
    }
    @Test
    void getEmptyCart() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart/get")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example2@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        //empty json
        assertEquals("[]", response);
    }
    @Test
    void getCartWithoutAuth() throws Exception {
        mockMvc.perform(get("/cart/get"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void editCartWithCorrectData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("quantity","4");

        MvcResult result = mockMvc.perform(post("/cart/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.UPDATED +"\"}", response);
    }
    @Test
    void editCartWithBadData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();

        MvcResult result = mockMvc.perform(post("/cart/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA +"\"}", response);
    }
    @Test
    void editCartWithBadFormatProduct() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");
        requestMap.put("quantity","0");
        MvcResult result = mockMvc.perform(post("/cart/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA +"\"}", response);
    }
    @Test
    void editCartWithBadQuantity() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("quantity","0");

        MvcResult result = mockMvc.perform(post("/cart/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA +"\"}", response);
    }
    @Test
    void editCartWithBadProduct() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","0");
        requestMap.put("quantity","4");

        MvcResult result = mockMvc.perform(post("/cart/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.PRODUCT_DONT_EXIST +"\"}", response);
    }
    @Test
    void editCartWithNonExistingItem() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-3");
        requestMap.put("quantity","4");

        MvcResult result = mockMvc.perform(post("/cart/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.ITEM_DONT_EXIST +"\"}", response);
    }
    @Test
    void editCartWithoutAuth() throws Exception {
        mockMvc.perform(post("/cart/edit"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}