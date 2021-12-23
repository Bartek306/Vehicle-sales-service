package com.example.demo.utils;

import com.example.demo.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtUtils {

    private String jwtSecret = "Secret";

    public String generateJwtToken(Authentication authentication){
        UserModel userModel = (UserModel) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userModel.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 100000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token);
        }catch (Exception exception){
            return false;
        }
        return true;
    }
}
