package com.foodtech.blog.security;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@Log
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String email){
        Date date =  Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token);
            return true;
        }catch (ExpiredJwtException expEx){
            log.severe("Token expired");
        }catch(UnsupportedJwtException unsEx){
            log.severe("Unsupported token");
        }catch (MalformedJwtException mEx){
            log.severe("Malformed jwt");
        }catch (SignatureException sEx){
            log.severe("invalid signature");
        }catch (Exception e){
            log.severe("invalid token");
        }
        return false;
    }

    public String getEmailFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
