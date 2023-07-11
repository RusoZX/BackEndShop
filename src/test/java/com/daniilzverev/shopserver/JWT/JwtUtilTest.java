package com.daniilzverev.shopserver.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class JwtUtilTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    JwtUtil underTest;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    String secret="TiteRuso";
    String username="example@example.com";
    String pwd="$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu";

    //Generate a token to test the methods with "example@example.com" and "SomeEncryptedData"
    final String exampleToken= newToken();

    final Claims exampleClaims = Jwts.parser().setSigningKey(secret).parseClaimsJws(exampleToken).getBody();

    Date issueDate= exampleClaims.getIssuedAt();
    Date expirationDate= exampleClaims.getExpiration();


    @Test
    void extractUserName() {
        assertEquals(username, underTest.extractUserName(exampleToken));
    }

    @Test
    void extractExpiration() {
        assertEquals(expirationDate, underTest.extractExpiration(exampleToken));
    }

    @Test
    void extractPwd() {
        assertEquals(pwd, underTest.extractPwd(exampleToken));
    }

    @Test
    void extractClaims() {
        assertEquals(exampleClaims, underTest.extractAllClaims(exampleToken));
    }

    @Test
    void extractAllClaims() {
        assertEquals(username,
                underTest.extractClaims(exampleToken, Claims::getSubject));
        assertEquals(issueDate,
                underTest.extractClaims(exampleToken, Claims::getIssuedAt));
        assertEquals(expirationDate,
                underTest.extractClaims(exampleToken, Claims::getExpiration));
    }

    @Test
    void generateToken() {
        String token = newToken();

        //Now we get the Claims of the new token and extract the info to verify the token
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        assertEquals(username, claims.getSubject());
        assertEquals(pwd, claims.get("pwd"));
    }

    @Test
    void validateExpiredToken() {
        String expiredToken="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlQGV4YW1wbGUuY29tIiwicHdkIjoiU29tZUVuY3J5cHRlZEluZm8i" +
                "LCJleHAiOjE2ODczODIxNjAsImlhdCI6MTY4NzM0NjE2MH0.o-5KsMCtwcmurAP1XvaJcx1F3gtUUKo36rA5Bw8TDd8";

        userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn(username);
        Mockito.when(userDetails.getPassword()).thenReturn(pwd);
        assertFalse(underTest.validateToken(expiredToken, userDetails));
    }
    @Test
    void validateToken() {
        userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn(username);
        Mockito.when(userDetails.getPassword()).thenReturn(pwd);
        assertTrue(underTest.validateToken(newToken(), userDetails));
    }

    private String newToken(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("pwd","someEncryptedData");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //With this we set the expiration in 10 hours
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }
}