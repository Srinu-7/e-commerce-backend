package com.zosh.e_commerce.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {

    private final SecretKey key;

    public JwtProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a secure key
    }

    public String generateToken(Authentication authentication) {
            String jwt = Jwts.builder()
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .claim("email",authentication.getName())
                    .signWith(key).compact();
            return jwt;
    }

    public String getEmailFromToken(String jwt) {

        jwt = jwt.substring(7);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        String email = String.valueOf(claims.get("email"));

        return email;
    }
}
