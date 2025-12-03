package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String GenerateToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", List.of(rol))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return parseClaims(token).getBody().get("roles", List.class);
    }

    public boolean validateJwtToken(String token) {
        Result result = new Result();

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            result.Object = "Invalid JWT signature: " + e.getMessage();
        } catch (MalformedJwtException e) {
            result.Object = "Invalid JWT signature: " + e.getMessage();
        } catch (ExpiredJwtException e) {
            result.Object = "JWT token is expired: " + e.getMessage();
        } catch (UnsupportedJwtException e) {
            result.Object = "JWT token is unsupported: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            result.Object = "JWT claims string is empty: " + e.getMessage();
        }
        return false;
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
