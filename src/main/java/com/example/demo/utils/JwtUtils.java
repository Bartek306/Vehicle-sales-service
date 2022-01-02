package com.example.demo.utils;

import com.example.demo.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class JwtUtils {

    private static String jwtSecret = "Secret";

    public String generateJwtToken(Authentication authentication){
        UserModel userModel = (UserModel) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userModel.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 100000 * 60))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public static String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public static String getUsernameFromHeader(){
        return getUsernameFromJwtToken(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization").substring(7));
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        }catch (Exception exception){
            return false;
        }
        return true;
    }
}
