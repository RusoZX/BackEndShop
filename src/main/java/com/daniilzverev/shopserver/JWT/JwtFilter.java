package com.daniilzverev.shopserver.JWT;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUsersDetailsService service;

    Claims claims = null;
    private String userName= null;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Here we check if the urls are the ones that dont need authentication
        if(httpServletRequest.getServletPath().matches("/user/login|/user/signup")){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else{

            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;
            //Here we check the authorization header and then extract the needed value from the token
            if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUserName(token);
                claims = jwtUtil.extractAllClaims(token);
            }
            if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                try {
                    UserDetails userDetails = service.loadUserByUsername(userName);
                    //Here we validate that the user has the correct credentials and a valid token
                    if (jwtUtil.validateToken(token, userDetails)) {
                        log.info("Token validated :" + userName);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null,
                                        userDetails.getAuthorities());

                        usernamePasswordAuthenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                        //And then we set the authentication to the server security
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }catch (UsernameNotFoundException e){
                    log.error("USER NOT FOUND"+ userName);
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        }
    }
    public String getCurrentUser(){
        return userName;
    }
}
