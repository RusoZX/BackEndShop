package com.daniilzverev.shopserver.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.daniilzverev.shopserver.utils.Utils.matches;

@Slf4j
@Service
public class JwtUtil {

    private String secret = "TiteRuso";

    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    public String extractPwd(String token){
        return (String) extractAllClaims(token).get("pwd");
    }

    //Here we create a generic method that can return any type of object depending on predetermined parameters for the function
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        //Uses Jwts parser to decrypt the token and get the info carried by it
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String pwd){
        Map<String, Object> claims = new HashMap<>();
        claims.put("pwd",pwd);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims,String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //With this we set the expiration in 10 hours
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        try {
            final String username= extractUserName(token);
            final String pwd = extractPwd(token);

            return username.equals(userDetails.getUsername())
                    && pwd.equals(userDetails.getPassword())
                    && !isTokenExpired(token);
        }catch(ExpiredJwtException ex){
            log.error("TOKEN EXPIRED :"+ex.getLocalizedMessage());
            return false;
        }
    }
}
