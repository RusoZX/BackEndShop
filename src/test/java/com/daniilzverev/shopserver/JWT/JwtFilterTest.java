package com.daniilzverev.shopserver.JWT;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {

    @Autowired
    JwtFilter underTest = new JwtFilter();
    @Test
    void doFilterInternalWithNoAuthNeeded() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // Set up the necessary headers
        Mockito.when(request.getServletPath()).thenReturn("/user/signup");
        //Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer <valid_jwt_token>");



        underTest.doFilterInternal(request, response, filterChain);

        // Verify the expected behavior
        Mockito.verify(filterChain).doFilter(request, response);
    }

    //ASk how to properly test this
    @Test
    void doFilterInternalWithAuthNeededAndBadToken() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // Set up the necessary headers
        Mockito.when(request.getServletPath()).thenReturn("/user/profile");
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer <valid_jwt_token>");

        underTest.doFilterInternal(request, response, filterChain);

        // Verify the expected behavior
        Mockito.verify(filterChain).doFilter(request, response);
    }
}