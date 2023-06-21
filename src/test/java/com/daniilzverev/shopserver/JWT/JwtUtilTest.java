package com.daniilzverev.shopserver.JWT;

import com.daniilzverev.shopserver.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Mock
    private UserDetails userDetails;

    JwtUtil underTest = new JwtUtil();

    //I've generated a token to test the methods with "example@example.com" and "SomeEncryptedInfo"
    final String exampleToken= "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlQGV4YW1wbGUuY29tIiwicHdkIjoiU29tZUVuY3J5cHRlZEluZm8i" +
            "LCJleHAiOjE2ODczODIxNjAsImlhdCI6MTY4NzM0NjE2MH0.o-5KsMCtwcmurAP1XvaJcx1F3gtUUKo36rA5Bw8TDd8";

    String username="example@example.com";
    String pwd="SomeEncryptedInfo";
    @Test
    void extractUserName() {
        assertEquals(username, underTest.extractUserName(exampleToken));
    }

    @Test
    void extractExpiration() {
        assertEquals(generateExampleDate("Wed Jun 21 23:16:00 CEST 2023"), underTest.extractExpiration(exampleToken));
    }

    @Test
    void extractPwd() {
        assertEquals(pwd, underTest.extractPwd(exampleToken));
    }

    @Test
    void extractClaims() {
        assertEquals(generateExampleClaims(), underTest.extractAllClaims(exampleToken));
    }

    @Test
    void extractAllClaims() {
        assertEquals(username,
                underTest.extractClaims(exampleToken, Claims::getSubject));
        assertEquals(generateExampleDate("Wed Jun 21 13:16:00 CEST 2023"),
                underTest.extractClaims(exampleToken, Claims::getIssuedAt));
        assertEquals(generateExampleDate("Wed Jun 21 23:16:00 CEST 2023"),
                underTest.extractClaims(exampleToken, Claims::getExpiration));
    }

    @Test
    void generateToken() {
        //given


        System.out.println(underTest.generateToken(username,pwd));
    }

    @Test
    void validateToken() {
        userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn(username);
        Mockito.when(userDetails.getPassword()).thenReturn(pwd);
        if(new Date().before(generateExampleDate("Wed Jun 21 23:16:00 CEST 2023")))
            assertTrue(underTest.validateToken(exampleToken, userDetails));
        else
            assertFalse(underTest.validateToken(exampleToken, userDetails));
    }

    private Date generateExampleDate(String date){
        String dateString = "2023-06-21 23:16:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
            return  Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Claims generateExampleClaims(){
        return Jwts.parser().setSigningKey("TiteRuso").parseClaimsJws(exampleToken).getBody();
    }
}