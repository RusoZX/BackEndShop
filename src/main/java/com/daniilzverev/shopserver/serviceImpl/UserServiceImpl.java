package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtFilter;
import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.JWT.CustomerUsersDetailsService;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.AddressDao;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.Address;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.service.UserService;
import com.daniilzverev.shopserver.utils.Utils;
import com.daniilzverev.shopserver.wrapper.AddressWrapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.daniilzverev.shopserver.utils.Utils.encode;
import static com.daniilzverev.shopserver.utils.Utils.matches;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    CustomerUsersDetailsService customerUserDetailsService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("New User Signing :"+requestMap);
        try {
            if (checkSignUpMap(requestMap))
                if (Objects.isNull(userDao.findByEmail(requestMap.get("email")))) {
                    userDao.save(getUserFromMap(requestMap));
                    return Utils.getResponseEntity(Constants.REGISTERED, HttpStatus.OK);
                } else
                    return Utils.getResponseEntity(Constants.USER_EXISTS, HttpStatus.BAD_REQUEST);
            else
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch(Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("User Trying to Log In:" + requestMap);
        try{
            if(checkLoginMap(requestMap)){
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("pwd"))
                );

                return new ResponseEntity<String>("{\"token\":\"" +
                        jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                customerUserDetailsService.getUserDetail().getPwd()) + "\"}"
                        , HttpStatus.OK);
            }else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch(BadCredentialsException e){
            return Utils.getResponseEntity(Constants.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<User> getUserData() {
        try{
            log.info("User Trying to get profile information"+jwtFilter.getCurrentUser());
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!Objects.isNull(user)){
                //now we create a new user with only the needed information
                User toSend = new User();
                toSend.setEmail(user.getEmail());
                toSend.setName(user.getName());
                toSend.setBirthDate(user.getBirthDate());
                toSend.setSurname(user.getSurname());

                return new ResponseEntity<User>(toSend, HttpStatus.OK);
            }

        }catch(Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        return new ResponseEntity<User>(new User(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProfile(Map<String, String> requestMap) {
        if(checkUpdateMap(requestMap))
            try{
                log.info("User "+jwtFilter.getCurrentUser()+" Trying to update profile information :"+requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());

                if(!Objects.isNull(user)){
                    if(requestMap.containsKey("name"))
                        user.setName(requestMap.get("name"));
                    if(requestMap.containsKey("surname"))
                        user.setSurname(requestMap.get("surname"));
                    if(requestMap.containsKey("birthday"))
                        user.setBirthDate(LocalDate.parse(requestMap.get("birthday"),formatter));

                    userDao.save(user);
                    return Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);
                }
            }catch(DateTimeParseException e){
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            catch(Exception ex){
                log.error(ex.getLocalizedMessage());
            }
        else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> changePwd(Map<String, String> requestMap) {
        try {
            if (checkChangePwdMap(requestMap)) {
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to change password :" + requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());

                if (!Objects.isNull(user))
                    if (matches(requestMap.get("oldPwd"), user.getPwd())) {
                        user.setPwd(Utils.encode(requestMap.get("newPwd")));
                        userDao.save(user);
                        return Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);

                    } else return Utils.getResponseEntity(Constants.INVALID_PWD, HttpStatus.BAD_REQUEST);
            } else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        try{
            log.info("User "+jwtFilter.getCurrentUser()+" Trying to get list of users");
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());

            if(!Objects.isNull(user))
                if(user.getRole().equals("employee"))
                    return new ResponseEntity<List<User>>(userDao.findAll(), HttpStatus.OK);
                else
                    return new ResponseEntity<List<User>>(new ArrayList<User>(), HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return new ResponseEntity<List<User>>(new ArrayList<User>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addAddress(Map<String, String> requestMap) {
        try {
            if (checkAddAddressMap(requestMap)) {
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to add an address :" + requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)){
                    Address address = new Address();
                    address.setCountry(requestMap.get("country"));
                    address.setCity(requestMap.get("city"));
                    address.setPostalCode(requestMap.get("postalCode"));
                    address.setStreet(requestMap.get("street"));
                    address.setHome(requestMap.get("home"));
                    address.setApartment(requestMap.get("apartment"));
                    address.setUser(user);

                    addressDao.save(address);
                    return Utils.getResponseEntity(Constants.ADDRESS_ADDED, HttpStatus.OK);
                }
            } else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> editAddress(Map<String, String> requestMap) {
        try {
            if (checkEditAddressMap(requestMap)) {
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to edit an address :" + requestMap);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)){
                    Optional<Address> optAddress = addressDao.findById(Long.parseLong(requestMap.get("addressId")));
                    if(optAddress.isPresent()){
                        if(user.equals(optAddress.get().getUser())){
                            Address address= optAddress.get();
                            if(requestMap.containsKey("country"))
                                address.setCountry(requestMap.get("country"));
                            if(requestMap.containsKey("city"))
                                address.setCity(requestMap.get("city"));
                            if(requestMap.containsKey("postalCode"))
                                address.setPostalCode(requestMap.get("postalCode"));
                            if(requestMap.containsKey("street"))
                                address.setStreet(requestMap.get("street"));
                            if(requestMap.containsKey("home"))
                                address.setHome(requestMap.get("home"));
                            if(requestMap.containsKey("apartment"))
                                address.setApartment(requestMap.get("apartment"));

                            addressDao.save(address);
                            return Utils.getResponseEntity(Constants.UPDATED, HttpStatus.OK);
                        }else
                            return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                    }else
                        return Utils.getResponseEntity(Constants.ADDRESS_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }
            } else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> removeAddress(String idAddress) {
        try {
            if (checkRemoveAddress(idAddress)) {
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to remove an address :" + idAddress);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)){
                    Optional<Address> optAddress = addressDao.findById(Long.parseLong(idAddress));
                    if(optAddress.isPresent()){
                        if(user.equals(optAddress.get().getUser())){
                            addressDao.delete(optAddress.get());
                            return Utils.getResponseEntity(Constants.REMOVED, HttpStatus.OK);
                        }else
                            return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                    }else
                        return Utils.getResponseEntity(Constants.ADDRESS_DONT_EXIST, HttpStatus.BAD_REQUEST);
                }
            } else return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<AddressWrapper>> getAllAddress() {
        try {
            log.info("User " + jwtFilter.getCurrentUser() + " Trying to get addresses");
            User user = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!Objects.isNull(user)){
                return new ResponseEntity<List<AddressWrapper>>(addressDao.findAllWrapper(user.getId()),
                        HttpStatus.OK);
            }
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return new ResponseEntity<List<AddressWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> getAddress(String addressId) {
        try {
            if(checkAddressId(addressId)) {
                log.info("User " + jwtFilter.getCurrentUser() + " Trying to get address "+addressId);
                User user = userDao.findByEmail(jwtFilter.getCurrentUser());
                if (!Objects.isNull(user)) {
                    Optional<Address> optAddress = addressDao.findById(Long.parseLong(addressId));
                    if(optAddress.isPresent()){
                        if(user.equals(optAddress.get().getUser())){
                            Address address = optAddress.get();
                            return new ResponseEntity<String>(address.toJson(), HttpStatus.OK);
                        }
                        else
                            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
                    }else
                        return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
                }
            }else
                return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            log.error(ex.getLocalizedMessage());
        }
        //It will only get to here through an error
        return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken(String token) {
        log.info("------------------------------------------Checking Token " + token );
        try {
            User user = userDao.findByEmail(jwtUtil.extractUserName(token));
            if (!Objects.isNull(user)) {
                if (jwtUtil.extractPwd(token).equals(user.getPwd())) {
                    return Utils.getResponseEntity(Constants.CHECKED, HttpStatus.OK);
                } else
                    return Utils.getResponseEntity(Constants.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
            } else
                return Utils.getResponseEntity(Constants.USER_DONT_EXIST, HttpStatus.BAD_REQUEST);
        }catch (ExpiredJwtException e){
            return Utils.getResponseEntity(Constants.EXPIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkChangePwdMap(Map<String,String> requestMap){
        return requestMap.containsKey("oldPwd")&&requestMap.containsKey("newPwd");
    }

    private boolean checkAddAddressMap(Map<String,String> requestMap){
        return requestMap.containsKey("country")&&requestMap.containsKey("city")
                &&requestMap.containsKey("postalCode")&&requestMap.containsKey("street")
                &&requestMap.containsKey("home")&&requestMap.containsKey("apartment");

    }
    private boolean checkEditAddressMap(Map<String,String> requestMap){
        if(requestMap.containsKey("addressId")&&(requestMap.containsKey("country")||requestMap.containsKey("city")
                ||requestMap.containsKey("postalCode")||requestMap.containsKey("street")
                ||requestMap.containsKey("home")||requestMap.containsKey("apartment")))
            try{
                Long.parseLong(requestMap.get("addressId"));
                return true;
            }catch (NumberFormatException e){
                log.error("Address id bad format");
            }
        return false;
    }
    private boolean checkRemoveAddress(String idAddress){
        if(!idAddress.isEmpty())
            try{
                Long.parseLong(idAddress);
                return true;
            }catch (NumberFormatException e){
                log.error("Address id bad format");
            }
        return false;
    }
    private boolean checkAddressId(String addressId){
        if(!Objects.isNull(addressId))
            try{
                Long.parseLong(addressId);
                return true;
            }catch (NumberFormatException e){
                log.error("Address id bad format");
            }
        return false;
    }

    private boolean checkUpdateMap(Map<String,String> requestMap){

        if(requestMap.containsKey("name")||requestMap.containsKey("surname")
                ||requestMap.containsKey("birthday")){
            if(requestMap.containsKey("birthday"))
                try{
                    LocalDate.parse(requestMap.get("birthday"), formatter);
                }catch (Exception e){
                    log.error("Bad date format"+e.getLocalizedMessage());
                    return false;
                }
            return true;
        }

        return false;
    }

    private boolean checkLoginMap(Map<String,String> requestMap){
        return requestMap.containsKey("email") && requestMap.containsKey("pwd");
    }

    private boolean checkSignUpMap(Map<String,String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("surname")
                && requestMap.containsKey("birthdate") && requestMap.containsKey("email")
                && requestMap.containsKey("pwd")){
            try {
                LocalDate.parse(requestMap.get("birthdate"), formatter);
                if(requestMap.get("name").isEmpty() || requestMap.get("surname").isEmpty() ||
                        requestMap.get("birthdate").isEmpty() || requestMap.get("email").isEmpty() ||
                         requestMap.get("pwd").isEmpty())
                    return false;
                return true;
            } catch (Exception e) {
                log.error("Bad date format"+e.getLocalizedMessage());
            }
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> map){
        User user = new User();

        user.setName(map.get("name"));
        user.setSurname(map.get("surname"));
        user.setEmail(map.get("email"));
        user.setPwd(encode(map.get("pwd")));
        user.setBirthDate(LocalDate.parse(map.get("birthdate"), formatter));
        user.setRole("client");

        return user;
    }
}
