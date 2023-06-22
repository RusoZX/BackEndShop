package com.daniilzverev.shopserver.JWT;

import com.daniilzverev.shopserver.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private com.daniilzverev.shopserver.entity.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Inside Service to load User by email:", email);
        userDetail = userDao.findByEmail(email);
        if(!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(), userDetail.getPwd(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found");
    }

    public com.daniilzverev.shopserver.entity.User getUserDetail(){
        return userDetail;
    }
}
