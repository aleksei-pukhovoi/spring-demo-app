package com.example.springappdemo.integrationTest;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.springappdemo.SpringAppDemoApplication;
import com.example.springappdemo.model.dto.AuthRequest;
import com.example.springappdemo.model.dto.AuthResponse;
import com.example.springappdemo.security.JWTUtil;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(classes = SpringAppDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class AuthControllerIntegrationTest {
  @LocalServerPort
  private int port;

  @Autowired
  private JWTUtil jwtUtil;

  private TestRestTemplate restTemplate;

  private HttpHeaders requestHeaders;

  private AuthRequest request;

  @BeforeEach
  public void setUp() {

    restTemplate = new TestRestTemplate();
    requestHeaders = new HttpHeaders();
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    request = new AuthRequest();
  }

  @Test
  public void givenAuthRequest_whenGet_thenReturnTokenAndStatus200() {
    UserDetails userDetails = new User("user", "password", new ArrayList<>());
    String expectedToken = jwtUtil.generateToken(userDetails);
    request.setName("user");
    request.setPassword("password");

    HttpEntity<Object> requestEntity = new HttpEntity<>(request, requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/authenticate")
        .build();

    //when
    ResponseEntity<AuthResponse> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.POST,
        requestEntity,
        AuthResponse.class
    );

    // then
    String actualToken = response.getBody().getJwtToken();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");
    assertThat(actualToken).isEqualTo(expectedToken);
  }

  @Test
  public void givenIncorrectAuthRequest_when_thenBadRequestAndReturnStatus401() {
    request.setName("user");
    request.setPassword("incorrect password");

    HttpEntity<Object> requestEntity = new HttpEntity<>(request, requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/authenticate")
        .build();

    //when
    ResponseEntity<AuthResponse> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.POST,
        requestEntity,
        AuthResponse.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
  }


}