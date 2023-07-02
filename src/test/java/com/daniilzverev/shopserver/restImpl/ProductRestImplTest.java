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

import static com.daniilzverev.shopserver.utils.Utils.requestMapToJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.daniilzverev.shopserver.utils.Utils.requestMapToJson;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/insert_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

class ProductRestImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void addProductWithCorrectData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.PRODUCT_ADDED+"\"}", response);
    }
    @Test
    void addProductWithoutEmployee() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.UNAUTHORIZED+"\"}", response);
    }
    @Test
    void addProductWithBadData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addProductWithBadFormatPrice() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","badFormat");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addProductWithBadFormatWeight() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","badFormat");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addProductWithBadFormatVolume() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","badFormat");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addProductWithBadFormatStock() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","badFormat");

        MvcResult result = mockMvc.perform(post("/product/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void addProductWithoutAuth() throws Exception{
        mockMvc.perform(post("/product/add"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void editProductWithCorrectData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("title","idk");
        requestMap.put("price","3.14");
        requestMap.put("category","idk1");
        requestMap.put("brand","idk2");
        requestMap.put("color","red");
        requestMap.put("weight","10.4");
        requestMap.put("volume","10.5");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.UPDATED+"\"}", response);
    }
    @Test
    void editProductWithoutEmployee() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.UNAUTHORIZED+"\"}", response);
    }
    @Test
    void editProductWithoutId() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("title","idk");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void editProductWithBadId() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");
        requestMap.put("stock","4");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }@Test
    void editProductWithoutData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void editProductWithBadFormatPrice() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("stock","price");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void editProductWithBadFormatWeight() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("weight","badFormat");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void editProductWithBadFormatVolume() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("volume","badFormat");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void editProductWithBadFormatStock() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");
        requestMap.put("stock","badFormat");

        MvcResult result = mockMvc.perform(post("/product/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void editProductWithoutAuth() throws Exception{
        mockMvc.perform(post("/product/edit"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void removeProduct() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1L");

        MvcResult result = mockMvc.perform(post("/product/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.REMOVED+"\"}", response);
    }
    @Test
    void removeProductWithoutEmployee() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1L");

        MvcResult result = mockMvc.perform(post("/product/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.UNAUTHORIZED+"\"}", response);
    }
    @Test
    void removeProductWithBadId() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","badFormat");

        MvcResult result = mockMvc.perform(post("/product/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void removeProductWithBadProduct() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","0");

        MvcResult result = mockMvc.perform(post("/product/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.PRODUCT_DONT_EXIST+"\"}", response);
    }
    @Test
    void removeProductWithoutData() throws Exception {
        Map<String,String> requestMap= new HashMap<>();

        MvcResult result = mockMvc.perform(post("/product/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    void removeProductWithoutAuth() throws Exception{
        mockMvc.perform(post("/product/remove"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void getProduct() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/-1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"id\":-1,\"title\":\"test3\",\"price\":10.0,\"category\"" +
                ":\"test1\",\"brand\":\"test\",\"color\":\"test2\",\"weight\":10.0,\"volume\"" +
                ":10.0,\"stock\":10}", response);
    }
    @Test
    void getBadProduct() throws Exception{
        mockMvc.perform(get("/product/0"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getProductBadFormat() throws Exception{
        mockMvc.perform(get("/product/badFormat"))
                .andExpect(status().isBadRequest());
    }
}