package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.CustomerUsersDetailsService;
import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.AddressDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Address;
import com.daniilzverev.shopserver.entity.Product;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@Sql(scripts = {"/insert_test_data_user_rest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_user_rest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserServiceImplTest {


    @Mock
    private UserDao userDao;

    @Mock
    private AddressDao addressDao;

    @Mock
    private CustomerUsersDetailsService customerUserDetailsService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtFilter jwtFilter;

    @InjectMocks
    private UserServiceImpl underTest;

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

        ResponseEntity<String> response = underTest.signUp(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.REGISTERED+"\"}" , response.getBody());
        verify(underTest.userDao).save(any(User.class));
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

        ResponseEntity<String> response = underTest.signUp(requestMap);


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

        ResponseEntity<String> response = underTest.signUp(requestMap);


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

        ResponseEntity<String> response = underTest.login(requestMap);

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

        ResponseEntity<String> response = underTest.login(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.BAD_CREDENTIALS+"\"}" , response.getBody());
    }
    @Test
    public void loginBadData() {
        Map<String, String> requestMap = new HashMap<>();

        ResponseEntity<String> response = underTest.login(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}" , response.getBody());
    }
    @Test
    public void getUserData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        User user = new User();
        user.setId(-1L);
        user.setName("test");
        user.setSurname("test1");
        user.setEmail("example1@example.com");
        user.setBirthDate(LocalDate.parse("2001-09-11",formatter));
        user.setPwd("someEncryptedData");
        user.setRole("client");

        when(userDao.findByEmail("example1@example.com")).thenReturn(user);

        User expected = new User();
        expected.setEmail(user.getEmail());
        expected.setName(user.getName());
        expected.setBirthDate(user.getBirthDate());
        expected.setSurname(user.getSurname());

        ResponseEntity<User> response = underTest.getUserData();

        assertEquals(expected.getEmail(), response.getBody().getEmail());
        assertEquals(expected.getName(), response.getBody().getName());
        assertEquals(expected.getBirthDate(), response.getBody().getBirthDate());
        assertEquals(expected.getSurname(), response.getBody().getSurname());
    }
    @Test
    public void updateOnlyName(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","idk");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.updateProfile(requestMap);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}" , response.getBody() );

        //Validation to see if it was actually updated
        User user = giveTestUser();
        user.setName("idk");
        verify(userDao).save(user);
    }
    @Test
    public void updateOnlySurname(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("surname","idk");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.updateProfile(requestMap);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}" , response.getBody() );

        //Validation to see if it was actually updated
        User user = giveTestUser();
        user.setSurname("idk");
        verify(userDao).save(user);
    }
    @Test
    public void updateOnlyBirthDay(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");


        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("birthday","2002-10-12");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.updateProfile(requestMap);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}" , response.getBody() );

        //Validation to see if it was actually updated
        User user = giveTestUser();
        user.setBirthDate(LocalDate.parse("2002-10-12", formatter));

        verify(userDao).save(user);
    }
    @Test
    public void updateAllData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name","idk");
        requestMap.put("surname","idk1");
        requestMap.put("birthday","2002-10-12");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.updateProfile(requestMap);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.UPDATED+"\"}" , response.getBody() );

        //Validation to see if it was actually updated
        User user = giveTestUser();
        user.setName("idk");
        user.setSurname("idk1");
        user.setBirthDate(LocalDate.parse("2002-10-12", formatter));

        verify(userDao).save(user);
    }
    @Test
    public void updateWithNoData(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.updateProfile(requestMap);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}" , response.getBody() );
    }
    @Test
    public void updateWithBadDate(){

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("birthday","badformat");
        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.updateProfile(requestMap);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}" , response.getBody() );
    }
    @Test
    public void changePwdWithBadData(){

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("surname","idk");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.changePwd(requestMap);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_DATA+"\"}" , response.getBody() );
    }
    @Test
    public void changePwdWithBadPwd(){

        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("oldPwd","idk");
        requestMap.put("newPwd","idk");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.changePwd(requestMap);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\""+Constants.INVALID_PWD+"\"}" , response.getBody() );
    }
    @Test
    public void getUsers(){
        when(jwtFilter.getCurrentUser()).thenReturn("employee@example.com");

        User user= giveTestUser();
        user.setId(-2L);
        user.setEmail("employee@example.com");
        user.setRole("employee");

        when(userDao.findByEmail("employee@example.com")).thenReturn(user);

        ResponseEntity<List<User>> response= underTest.getUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    public void getUsersWithoutAuth(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<List<User>> response= underTest.getUsers();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void addAddress(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk");
        requestMap.put("city","idk");
        requestMap.put("postalCode","idk");
        requestMap.put("street","idk");
        requestMap.put("home","idk");
        requestMap.put("apartment","idk");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        ResponseEntity<String> response= underTest.addAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.ADDRESS_ADDED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(any());

    }
    @Test
    public void editAddressCountry(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("country","idk1");
        requestMap.put("addressId","-1");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Address address = giveTestAddress();
        address.setCountry("idk1");
        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.editAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.UPDATED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(address);

    }
    @Test
    public void editAddressCity(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("city","idk1");
        requestMap.put("addressId","-1");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Address address = giveTestAddress();
        address.setCity("idk1");

        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.editAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.UPDATED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(address);

    }
    @Test
    public void editAddressPostalCode(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("postalCode","idk1");
        requestMap.put("addressId","-1");

        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Address address = giveTestAddress();
        address.setPostalCode("idk1");

        ResponseEntity<String> response= underTest.editAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.UPDATED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(address);

    }
    @Test
    public void editAddressStreet(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("street","idk1");
        requestMap.put("addressId","-1");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Address address = giveTestAddress();
        address.setStreet("idk1");

        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.editAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.UPDATED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(address);

    }
    @Test
    public void editAddressHome(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("home","idk1");
        requestMap.put("addressId","-1");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Address address = giveTestAddress();
        address.setHome("idk1");

        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.editAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.UPDATED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(address);

    }
    @Test
    public void editAddressApartment(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("apartment","idk1");
        requestMap.put("addressId","-1");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        Address address = giveTestAddress();
        address.setApartment("idk1");

        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.editAddress(requestMap);
        assertEquals(Utils.getResponseEntity(Constants.UPDATED,HttpStatus.OK), response);

        verify(underTest.addressDao).save(address);
    }
    @Test
    public void deleteAddress(){
        when(jwtFilter.getCurrentUser()).thenReturn("example1@example.com");

        when(userDao.findByEmail("example1@example.com")).thenReturn(giveTestUser());

        when(addressDao.findById(-1L)).thenReturn(Optional.of(giveTestAddress()));

        ResponseEntity<String> response= underTest.removeAddress("-1");

        assertEquals(Utils.getResponseEntity(Constants.REMOVED,HttpStatus.OK), response);

        verify(underTest.addressDao).delete(giveTestAddress());
    }
    private User giveTestUser(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        User user = new User();
        user.setId(-1L);
        user.setName("test");
        user.setSurname("test1");
        user.setEmail("example1@example.com");
        user.setBirthDate(LocalDate.parse("2001-09-11",formatter));
        user.setPwd("someEncryptedData");
        user.setRole("client");

        return user;
    }
    private Address giveTestAddress(){
        Address address= new Address();
        address.setId(-1L);
        address.setCountry("idk");
        address.setCity("idk");
        address.setPostalCode("idk");
        address.setStreet("idk");
        address.setHome("idk");
        address.setApartment("idk");
        address.setUser(giveTestUser());

        return address;
    }


}