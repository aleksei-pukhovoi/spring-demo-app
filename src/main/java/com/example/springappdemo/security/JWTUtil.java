package com.example.springappdemo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

  @Value("$jwt.secret")
  private String secretKey;

  public String generateToken(UserDetails userDetails) {
    return JWT.create()
        .withSubject(userDetails.getUsername())
        .sign(Algorithm.HMAC512(secretKey.getBytes(StandardCharsets.UTF_8)));
  }

  public String getUserNameFromToken(String token) {
    return JWT.require(Algorithm.HMAC512(secretKey.getBytes(StandardCharsets.UTF_8)))
        .build()
        .verify(token)
        .getSubject();
  }
}
