package edu.ijse.jwt_spring.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class JwtUtil {

    @Value(("${jwt.expiration}"))
    private  long expiration;
    @Value(("${jwt.secret}"))
    private  String secretKey;


    public String generateToken(String name){
    String token = Jwts.builder().setSubject(name).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expiration))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),SignatureAlgorithm.HS256).compact();
    System.out.println(token);
        return token;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String extractUsername(String token){
        return   Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(token).getBody().getSubject();
    }
}
