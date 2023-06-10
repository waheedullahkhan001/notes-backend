package com.cloud.notesbackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject("Authentication")
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 mints
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT getDecodedToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }
}
