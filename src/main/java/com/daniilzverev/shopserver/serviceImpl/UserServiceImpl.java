package com.daniilzverev.shopserver.serviceImpl;

import com.daniilzverev.shopserver.JWT.JwtUtil;
import com.daniilzverev.shopserver.JWT.CustomerUsersDetailsService;
import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.dao.UserDao;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.service.UserService;
import com.daniilzverev.shopserver.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtUtil jwtUtil;

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
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("User Trying to Log In:" + requestMap);
        try{
            if(checkLoginMap(requestMap)){
                Authentication auth= authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("pwd"))
                );
                if(auth.isAuthenticated()){
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}"
                            , HttpStatus.OK);
                }else
                    return Utils.getResponseEntity(Constants.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
            }else
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
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
                log.error("Data format invalid");
                return true;
            } catch (Exception e) {
                return false;
            }
        }else return false;
    }

    private User getUserFromMap(Map<String, String> map){
        User user = new User();

        user.setName(map.get("name"));
        user.setSurname(map.get("surname"));
        user.setEmail(map.get("email"));
        //Password should be sent encrypted
        user.setPwd(map.get("pwd"));
        user.setBirthDate(LocalDate.parse(map.get("birthdate"), formatter));
        user.setRole("client");

        return user;
    }
}
