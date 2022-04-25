package com.example.springappdemo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  public JWTFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer_")) {
      chain.doFilter(request, response);
      return;
    }
    String jwt = authorizationHeader.substring(7);
    String username = jwtUtil.getUserNameFromToken(jwt);
    if (Objects.nonNull(username)) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    chain.doFilter(request, response);
  }
}

