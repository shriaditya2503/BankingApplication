package com.project.BankingApplication.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY_STRING = "adityahudfhdufhudfguhhguifghguhghgushgfgfgkhfgjkhfjkgisiddhant";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_STRING));

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*5))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            return false; // Token expired
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false; // Token is malformed
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
            return false; // Token unsupported
        } catch (JwtException e) {
            System.out.println("JWT exception: " + e.getMessage());
            return false; // General JWT exception
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token");
        }
    }
}
