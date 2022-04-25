package com.example.springappdemo.model.dto;

public class AuthResponse {

  private String jwtToken;

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }
}
