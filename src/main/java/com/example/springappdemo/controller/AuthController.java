package com.example.springappdemo.controller;

import com.example.springappdemo.model.dto.AuthRequest;
import com.example.springappdemo.model.dto.AuthResponse;
import com.example.springappdemo.security.JWTUtil;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;

  public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> createToken(@RequestBody AuthRequest authRequest)
      throws Exception {
    Authentication authentication;

    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequest.getName(),
              authRequest.getPassword(),
              new ArrayList<>()));
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Username or password is incorrect");
    }
    String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
    AuthResponse authResponse = new AuthResponse();
    authResponse.setJwtToken(jwt);
    return new ResponseEntity(authResponse, HttpStatus.OK);

  }
}
