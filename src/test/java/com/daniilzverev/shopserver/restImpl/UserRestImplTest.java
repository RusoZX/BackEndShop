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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.daniilzverev.shopserver.utils.Utils.requestMapToJson;

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

        mockMvc.perform(post("/user/login")
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
    public void getProfileWithoutAuth() throws Exception{

        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    public void updateWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/profile/update"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    public void updateWithBadData() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email","example1@example.com");

        MvcResult result = mockMvc.perform(post("/user/profile/update")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    public void updateCorrectly() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","idk");
        requestMap.put("surname","idk");

        MvcResult result = mockMvc.perform(post("/user/profile/update")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}", response);
    }

    @Test
    public void changePwdWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/profile/changepwd"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void changePwdWithBadData() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email","example1@example.com");

        MvcResult result = mockMvc.perform(post("/user/profile/changepwd")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}", response);
    }
    @Test
    public void changePwdWithBadPwd() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("oldPwd","example1@example.com");
        requestMap.put("newPwd","newPwd");

        MvcResult result = mockMvc.perform(post("/user/profile/changepwd")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_PWD+"\"}", response);
    }
    @Test
    public void changePwd() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("oldPwd","someEncryptedData");
        requestMap.put("newPwd","newPwd");

        MvcResult result = mockMvc.perform(post("/user/profile/changepwd")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}", response);
    }
    @Test
    public void getUsersWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/users"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    public void getUsersBeingAClient() throws Exception{mockMvc.perform(get("/user/users")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData")))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
    @Test
    public void getUsers() throws Exception{
        MvcResult result = mockMvc.perform(get("/user/users")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-2,\"name\":\"test\",\"surname\":\"test1\",\"birthDate\":" +
                "\"2001-09-11\",\"email\":\"employee@example.com\",\"pwd\":\"someEncryptedData\"," +
                "\"role\":\"employee\"},{\"id\":-1,\"name\":\"test\",\"surname\":\"test1\",\"birthDate" +
                "\":\"2001-09-11\",\"email\":\"example1@example.com\",\"pwd\":\"someEncryptedData\"," +
                "\"role\":\"client\"}]", response);
    }
    @Test
    public void addAddress() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("city","idk");
        requestMap.put("postalCode","idk");
        requestMap.put("street","idk");
        requestMap.put("home","idk");
        requestMap.put("apartment","idk");
        requestMap.put("userId","-1");

        MvcResult result = mockMvc.perform(get("/user/address/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.ADDRESS_ADDED+"\"}"
                , response);
    }
    @Test
    public void addAddressBadFormat() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("city","idk");


        MvcResult result = mockMvc.perform(post("/user/address/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void addAddressBadIdFormat() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("city","idk");
        requestMap.put("postalCode","idk");
        requestMap.put("street","idk");
        requestMap.put("home","idk");
        requestMap.put("apartment","idk");
        requestMap.put("userId","badFormat");

        MvcResult result = mockMvc.perform(post("/user/address/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void addAddressBadUser() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("city","idk");
        requestMap.put("postalCode","idk");
        requestMap.put("street","idk");
        requestMap.put("home","idk");
        requestMap.put("apartment","idk");
        requestMap.put("userId","0");

        MvcResult result = mockMvc.perform(post("/user/address/add")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void addAddressWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/address/add"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    public void editAddress() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk1");
        requestMap.put("addressId","-1");

        MvcResult result = mockMvc.perform(get("/user/address/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}"
                , response);
    }
    @Test
    public void editAddressBadFormat() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("city","idk");


        MvcResult result = mockMvc.perform(post("/user/address/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void editAddressBadIdFormat() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("addressId","badFormat");

        MvcResult result = mockMvc.perform(post("/user/address/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void editAddressBadUser() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk1");
        requestMap.put("addressId","-3");

        MvcResult result = mockMvc.perform(post("/user/address/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void editAddressBadAddress() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk1");
        requestMap.put("addressId","0");

        MvcResult result = mockMvc.perform(post("/user/address/edit")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.ADDRESS_DONT_EXIST+"\"}"
                , response);
    }
    @Test
    public void editAddressWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/address/edit"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    public void removeAddress() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("addressId","-1");

        MvcResult result = mockMvc.perform(get("/user/address/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.REMOVED+"\"}"
                , response);
    }
    @Test
    public void removeAddressBadFormat() throws Exception{
        Map<String, String> requestMap = new HashMap<>();

        MvcResult result = mockMvc.perform(post("/user/address/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void removeAddressBadIdFormat() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("addressId","badFormat");

        MvcResult result = mockMvc.perform(post("/user/address/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void removeAddressBadUser() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("addressId","-3");

        MvcResult result = mockMvc.perform(post("/user/address/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}"
                , response);
    }
    @Test
    public void removeAddressBadAddress() throws Exception{
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("addressId","0");

        MvcResult result = mockMvc.perform(post("/user/address/remove")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestMapToJson(requestMap)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\""+Constants.ADDRESS_DONT_EXIST+"\"}"
                , response);
    }
    @Test
    public void removeAddressWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/address/remove"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    public void getAllAddress() throws Exception{

        MvcResult result = mockMvc.perform(get("/user/address/getAll")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-2,\"city\":\"idk1\",\"street\":\"idk1\"}," +
                        "{\"id\":-1,\"city\":\"idk\",\"street\":\"idk\"}]", response);
    }
    @Test
    public void getAllAddress2() throws Exception{

        MvcResult result = mockMvc.perform(get("/user/address/getAll")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("[{\"id\":-3,\"city\":\"idk1\",\"street\":\"idk1\"}]", response);
    }
    @Test
    public void getAllAddressWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/address/getAll"))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    //Fix the appearing user
    @Test
    public void getAddress() throws Exception{
        MvcResult result = mockMvc.perform(get("/user/address/get-1")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();

        assertEquals("{\"id\":-1,\"country\":\"idk\",\"city\":\"idk\"," +
                "\"postalCode\":\"idk\",\"street\":\"idk\",\"home\":\"idk\",\"apartment\":\"idk\"}", response);
    }
    @Test
    public void getAddressBadFormat() throws Exception{
        mockMvc.perform(get("/user/address/get")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getAddressBadAddress() throws Exception{
        mockMvc.perform(get("/user/address/get0")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("example1@example.com","someEncryptedData")))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getAddressNotFromUser() throws Exception{
        mockMvc.perform(get("/user/address/get-1")
                .header("Authorization", "Bearer "
                        +jwtUtil.generateToken("employee@example.com","someEncryptedData")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getAddressWithoutAuth() throws Exception{
        mockMvc.perform(post("/user/address/get-1"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

}