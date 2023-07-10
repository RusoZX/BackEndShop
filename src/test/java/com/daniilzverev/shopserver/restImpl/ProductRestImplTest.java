package com.daniilzverev.shopserver.restImpl;

import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static com.daniilzverev.shopserver.utils.Utils.requestMapToJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.daniilzverev.shopserver.utils.Utils.requestMapToJson;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/insert_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("withJwtUtilAutowired")
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
    void removeProduct() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productId","-1");

        MvcResult result = mockMvc.perform(delete("/product/remove")
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

        MvcResult result = mockMvc.perform(delete("/product/remove")
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

        MvcResult result = mockMvc.perform(delete("/product/remove")
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

        MvcResult result = mockMvc.perform(delete("/product/remove")
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

        MvcResult result = mockMvc.perform(delete("/product/remove")
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
    void changeCategories() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productList","[{\"id\":\"-15\"},{\"id\":\"-14\"}]");
        requestMap.put("newCategory","newCategory");

        MvcResult result = mockMvc.perform(post("/product/changeCategories")
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
    void changeCategoriesBadFormat1() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productList","[{\"id\":\"-1\"},{\"id\":\"badFormat\"}]");
        requestMap.put("newCategory","newCategory");

        MvcResult result = mockMvc.perform(post("/product/changeCategories")
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
    void changeCategoriesBadFormat2() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("newCategory","newCategory");

        MvcResult result = mockMvc.perform(post("/product/changeCategories")
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
    void changeCategoriesBadFormat3() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productList","[{\"id\":\"-15\"},{\"id\":\"0\"}]");
        requestMap.put("newCategory","newCategory");

        MvcResult result = mockMvc.perform(post("/product/changeCategories")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isNotFound())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.PRODUCT_DONT_EXIST+"\"}", response);
    }
    @Test
    void changeCategoriesBadAuth() throws Exception {
        Map<String,String> requestMap= new HashMap<>();
        requestMap.put("productList","[{\"id\":\"-15\"},{\"id\":\"-14\"}]");
        requestMap.put("newCategory","newCategory");

        MvcResult result = mockMvc.perform(post("/product/changeCategories")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.UNAUTHORIZED+"\"}", response);
    }
    //Fix those tests


}

/*I had to separate the tests between the ones that need a token an the ones that dont need it
because when i run all the tests it creates the JwtUtils class empty when its not used and it gives problems */

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/insert_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_product.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("withoutJwtUtil")
class TestWithoutJwtUtil {
    @Autowired
    private MockMvc mockMvc;
    @Test
    void getProduct() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/-1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"id\":-1,\"title\":\"test3\",\"price\":1.0,\"category\"" +
                ":\"test1\",\"brand\":\"test\",\"color\":\"test2\",\"weight\":10.0,\"volume\"" +
                ":10.0,\"stock\":10,\"totalSold\":5}", response);
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
    @Test
    void getProductsBadUrl1() throws Exception{
        mockMvc.perform(get("/product/getBy"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getProductsBadUrl2() throws Exception{
        mockMvc.perform(get("/product/getByIDK"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getProductsBadUrl3() throws Exception{
        mockMvc.perform(get("/product/getByNone"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getProductsBadUrl4() throws Exception{
        mockMvc.perform(get("/product/getByNone?limit=idk"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getProductsBadUrl5() throws Exception{
        mockMvc.perform(get("/product/getByCategory?limit=5"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getProductsByNoneLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByNone?limit=5"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}" +
                ",{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByNoneLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByNone?limit=10"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}," +
                "{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}," +
                "{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}" +
                ",{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}" +
                ",{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByCategoryLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByCategory?limit=5&search=category"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}," +
                "{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}," +
                "{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}," +
                "{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByCategoryLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByCategory?limit=10&search=category"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}," +
                "{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}," +
                "{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}," +
                "{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByPriceDescLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByPriceDesc?limit=5"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}" +
                ",{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByPriceDescLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByPriceDesc?limit=10"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}," +
                "{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}," +
                "{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}," +
                "{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}," +
                "{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByPriceAscLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByPriceAsc?limit=5"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-1,\"title\":\"test3\",\"price\":1.0,\"stock\":10}," +
                "{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}," +
                "{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}," +
                "{\"id\":-4,\"title\":\"test4\",\"price\":4.0,\"stock\":10}," +
                "{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByPriceAscLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByPriceAsc?limit=10"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-1,\"title\":\"test3\",\"price\":1.0,\"stock\":10}" +
                ",{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}" +
                ",{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}" +
                ",{\"id\":-4,\"title\":\"test4\",\"price\":4.0,\"stock\":10}" +
                ",{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}" +
                ",{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}" +
                ",{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}" +
                ",{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}" +
                ",{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}" +
                ",{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}]", response);
    }

    @Test
    void getProductsByBrandLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByBrand?limit=5&search=brand"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}," +
                "{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}," +
                "{\"id\":-4,\"title\":\"test4\",\"price\":4.0,\"stock\":10}," +
                        "{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}]",
                response);
    }
    @Test
    void getProductsByBrandLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByBrand?limit=10&search=test1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}" +
                ",{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}," +
                "{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}," +
                "{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}," +
                "{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByColorLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByColor?limit=5&search=color"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}" +
                ",{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\"" +
                ",\"price\":8.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByColorLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByColor?limit=10&search=test3"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}," +
                "{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}," +
                "{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}," +
                "{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}," +
                "{\"id\":-4,\"title\":\"test4\",\"price\":4.0,\"stock\":10}," +
                "{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}," +
                "{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByTitleLimit5() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByTitle?limit=5&search=title"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}" +
                ",{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByTitleLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByTitle?limit=10&search=test4"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-4,\"title\":\"test4\",\"price\":4.0,\"stock\":10}," +
                "{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}," +
                "{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByBestSellersLimit10() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByBestSellers?limit=10"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}," +
                "{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}," +
                "{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}," +
                "{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}," +
                "{\"id\":-1,\"title\":\"test3\",\"price\":1.0,\"stock\":10}]", response);
    }
    @Test
    void getProductsByBestSellersLimit15() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getByBestSellers?limit=15"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-12,\"title\":\"title\",\"price\":12.0,\"stock\":10}," +
                "{\"id\":-15,\"title\":\"title\",\"price\":15.0,\"stock\":10}," +
                "{\"id\":-14,\"title\":\"title\",\"price\":14.0,\"stock\":10}," +
                "{\"id\":-11,\"title\":\"title\",\"price\":11.0,\"stock\":10}," +
                "{\"id\":-3,\"title\":\"test4\",\"price\":3.0,\"stock\":10}," +
                "{\"id\":-10,\"title\":\"test4\",\"price\":10.0,\"stock\":10}," +
                "{\"id\":-9,\"title\":\"test3\",\"price\":9.0,\"stock\":10}," +
                "{\"id\":-8,\"title\":\"test3\",\"price\":8.0,\"stock\":10}," +
                "{\"id\":-2,\"title\":\"test4\",\"price\":2.0,\"stock\":10}," +
                "{\"id\":-1,\"title\":\"test3\",\"price\":1.0,\"stock\":10}," +
                "{\"id\":-4,\"title\":\"test4\",\"price\":4.0,\"stock\":10}," +
                "{\"id\":-13,\"title\":\"title\",\"price\":13.0,\"stock\":10}," +
                "{\"id\":-7,\"title\":\"test3\",\"price\":7.0,\"stock\":10}," +
                "{\"id\":-6,\"title\":\"test3\",\"price\":6.0,\"stock\":10}," +
                "{\"id\":-5,\"title\":\"test3\",\"price\":5.0,\"stock\":10}]", response);
    }
    @Test
    void getCategories() throws Exception{
        MvcResult result = mockMvc.perform(get("/product/getCategories"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[\"test2\",\"category\",\"test1\"]", response);
    }


}
