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



@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/insert_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderRestImplTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JwtUtil jwtUtil;
    @Test
    void createOrder() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("deliveryMethod","delivery");
        requestMap.put("addressId","-1");
        requestMap.put("paymentMethod","cash");
        requestMap.put("goods","[{\"productId\":\"-1\",\"quantity\":\"1\"},{\"productId\":\"-2\",\"quantity\":\"1\"}]");

        MvcResult result = mockMvc.perform(post("/order/create")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+ Constants.ORDER_CREATED+"\"}", response);
    }
    @Test
    void createOrderBadFormat1() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("deliveryMethod","delivery");
        requestMap.put("addressId","-1");
        requestMap.put("paymentMethod","cash");

        MvcResult result = mockMvc.perform(post("/order/create")
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
    void createOrderBadFormat2() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("deliveryMethod","delivery");
        requestMap.put("addressId","badFormat");
        requestMap.put("paymentMethod","cash");
        requestMap.put("goods","[{\"productId\":\"-1\",\"quantity\":\"1\"},{\"productId\":\"-2\",\"quantity\":\"1\"}]");

        MvcResult result = mockMvc.perform(post("/order/create")
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
    void createOrderBadFormat3() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("deliveryMethod","delivery");
        requestMap.put("addressId","-1");
        requestMap.put("paymentMethod","cash");
        requestMap.put("goods","[{\"productId\":\"BadFormat\",\"quantity\":\"1\"},{\"productId\":\"-2\",\"quantity\":\"1\"}]");

        MvcResult result = mockMvc.perform(post("/order/create")
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
    void getOrder() throws Exception{
        MvcResult result = mockMvc.perform(get("/order/get-1")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"paymentStatus\":false,\"orderStatus\":\"pending\",\"paymentMethod\":\"cash\"," +
                "\"deliveryMethod\":\"delivery\",\"dateCreated\":\"2023-07-06\",\"addressId\":-1}", response);
    }
    @Test
    void getOrderBadFormat() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/get")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("example1@example.com", "someEncryptedData")))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("{\"paymentStatus\":false,\"orderStatus\":null,\"paymentMethod\":null," +
                "\"deliveryMethod\":null,\"dateCreated\":null,\"addressId\":null}", response);
    }
    @Test
    void getOrderBadOrder() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/get23")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("example1@example.com", "someEncryptedData")))
                .andExpect(status().isNotFound())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("{\"paymentStatus\":false,\"orderStatus\":null,\"paymentMethod\":null," +
                "\"deliveryMethod\":null,\"dateCreated\":null,\"addressId\":null}", response);
    }

    @Test
    void getAllOrders() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/getAll")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("example1@example.com", "someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("[{\"id\":-1,\"paymentStatus\":\"false\",\"orderStatus\":\"pending\"" +
                ",\"dateCreated\":\"2023-07-06\"}]", response);
    }

    @Test
    void updatePaymentStatus() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("update","payment");
        requestMap.put("updateData","true");
        requestMap.put("orderId","-1");

        MvcResult result = mockMvc.perform(post("/order/updateStatus")
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
    void updateStatus() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("update","status");
        requestMap.put("updateData","delivered");
        requestMap.put("orderId","-1");

        MvcResult result = mockMvc.perform(post("/order/updateStatus")
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
    void updateStatusBadFormat1() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("update","status");
        requestMap.put("orderId","-1");

        MvcResult result = mockMvc.perform(post("/order/updateStatus")
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
    void updateStatusBadFormat2() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("update","badFormat");
        requestMap.put("updateData","delivered");
        requestMap.put("orderId","-1");

        MvcResult result = mockMvc.perform(post("/order/updateStatus")
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
    void updateStatusBadFormat4() throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("update","status");
        requestMap.put("updateData","delivered");
        requestMap.put("orderId","badFormat");

        MvcResult result = mockMvc.perform(post("/order/updateStatus")
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
    void getAllOrdersEmployee() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/getAllOrders")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("employee@example.com", "someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals(
                "[{\"id\":-1,\"userName\":\"example1@example.com\",\"paymentStatus\":\"false\"," +
                        "\"orderStatus\":\"pending\",\"createdDate\":\"2023-07-06\",\"totalRevenue\":20.0}," +
                        "{\"id\":-2,\"userName\":\"example@example.com\",\"paymentStatus\":\"true\"," +
                        "\"orderStatus\":\"paid\",\"createdDate\":\"2023-06-11\",\"totalRevenue\":10.0}]", response);
    }
    @Test
    void getAllOrdersMonth() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/getAllOrders?search=month")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("employee@example.com", "someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("[{\"id\":-1,\"userName\":\"example1@example.com\",\"paymentStatus\":\"false\"," +
                "\"orderStatus\":\"pending\",\"createdDate\":\"2023-07-06\",\"totalRevenue\":20.0}," +
                "{\"id\":-2,\"userName\":\"example@example.com\",\"paymentStatus\":\"true\"," +
                "\"orderStatus\":\"paid\",\"createdDate\":\"2023-06-11\",\"totalRevenue\":10.0}]", response);
    }
    @Test
    void getAllOrdersWeek() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/getAllOrders?search=week")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("employee@example.com", "someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("[{\"id\":-1,\"userName\":\"example1@example.com\",\"paymentStatus\":\"false\"" +
                ",\"orderStatus\":\"pending\",\"createdDate\":\"2023-07-06\",\"totalRevenue\":20.0}," +
                "{\"id\":-2,\"userName\":\"example@example.com\",\"paymentStatus\":\"true\"," +
                "\"orderStatus\":\"paid\",\"createdDate\":\"2023-06-11\",\"totalRevenue\":10.0}]", response);
    }
    @Test
    void getAllOrdersNoAuth() throws Exception {
        mockMvc.perform(get("/order/getAllOrders")
                .header("Authorization", "Bearer "
                        + jwtUtil.generateToken("example1@example.com", "someEncryptedData")))
                .andExpect(status().isUnauthorized());
    }
}