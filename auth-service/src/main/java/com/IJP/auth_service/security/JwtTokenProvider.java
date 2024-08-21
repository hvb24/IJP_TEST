package com.IJP.auth_service.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private String jwtSecret = "87ed8805c0e3f289607d5cfdca7a13bd32243e5e5479d8cf3f71bfb641b0d82f";
    private Long jwtExpirationDate = 604800L;
    // generate token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date curentDate = new Date();
        Date expireDate = new Date(curentDate.getTime() + jwtExpirationDate);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Build the JWT token including roles as a claim
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)  // Include roles in the token claims
                .setIssuedAt(curentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;


    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // get username from jwt token
    public String getUsername(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate jwt token
    /**
     * token expire
     * invalid token
     * unsupported token
     * jwt claim is null or empty
     * */
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        }catch (MalformedJwtException exception){
            throw new RuntimeException("invalid jwt token");
        }catch (ExpiredJwtException exception){
            throw new RuntimeException("expiration jwt token");
        }catch (UnsupportedJwtException exception){
            throw new RuntimeException("unsupported jwt token");
        }catch (IllegalArgumentException exception){
            throw new RuntimeException("jwt claims string in null or empty");
        }
    }
}
