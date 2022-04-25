package com.example.springappdemo.unitTest.controller;

import com.example.springappdemo.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.springappdemo.model.dto.AuthRequest;
import com.example.springappdemo.model.dto.AuthResponse;
import com.example.springappdemo.security.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  private MockMvc mockMvc;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JWTUtil jwtUtil;

  @InjectMocks
  private AuthController authController;

  private AuthRequest authRequest;

  private ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController)
        .build();
    authRequest = new AuthRequest();
    authRequest.setName("user");
    authRequest.setPassword("password");
    mapper = new ObjectMapper();
  }

  @Test
  public void givenRequest_whenCreate_thenStatus200AndJwtTokenReturned()
      throws Exception {
    //given
    AuthResponse authResponse = new AuthResponse();
    authResponse.setJwtToken("token");
    String jsonRequest = mapper.writeValueAsString(authRequest);
    String expectedJsonResponse = mapper.writeValueAsString(authResponse);
    UserDetails currentUser = new User("user", "password", new ArrayList<>());
    when(jwtUtil.generateToken(currentUser)).thenReturn("token");
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(new UsernamePasswordAuthenticationToken(
            currentUser,
            null,
            new ArrayList<>()));

    //when
    MvcResult result = mockMvc.perform(post("/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();
    String actualJsonResponse = result.getResponse().getContentAsString();
    assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    verify(jwtUtil, times(1)).generateToken(any(UserDetails.class));
    verifyNoMoreInteractions(jwtUtil);
  }

  @Test
  public void givenBadRequest_whenIsNotExist_thenBadRequestReturned()
      throws Exception {
    //given
    AuthRequest authRequest = new AuthRequest();
    authRequest.setName("user");
    authRequest.setPassword("password");
    String jsonRequest = mapper.writeValueAsString(authRequest);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(BadCredentialsException.class);

    //when
    mockMvc.perform(post("/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))

        //then
        .andExpect(status().isUnauthorized());
  }
}