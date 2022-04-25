package com.example.springappdemo.integrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springappdemo.SpringAppDemoApplication;
import com.example.springappdemo.model.dto.DataMessage;
import com.example.springappdemo.model.dto.MessageDto;
import com.example.springappdemo.security.JWTUtil;
import com.example.springappdemo.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
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
class MessageControllerIntegrationTest {
  @LocalServerPort
  private int port;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private MessageService service;

  private TestRestTemplate restTemplate;

  private HttpHeaders requestHeaders;

  private DataMessage dataMessage;

  private UserDetails userDetails;

  @BeforeEach
  public void setUp() {
    userDetails = new User("user", "password", new ArrayList<>());
    restTemplate = new TestRestTemplate();
    requestHeaders = new HttpHeaders();
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
  }

  @Test
  public void givenUser_whenCreate_thenReturnMessageDtoAndStatus201() {
    //given
    dataMessage = getDataMessage("message", 1);
    requestHeaders.add(
        "Authorization",
        "Bearer_" + jwtUtil.generateToken(userDetails));
    HttpEntity<Object> requestEntity = new HttpEntity<>(dataMessage, requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/message")
        .build();

    //when
    ResponseEntity<MessageDto> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.POST,
        requestEntity,
        MessageDto.class
    );

    // then
    MessageDto actualMessage = response.getBody();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");
    assertThat(actualMessage.getMessage()).isEqualTo("message1");
    assertThat(actualMessage.getUserId()).isEqualTo(1);
  }

  @Test
  public void givenUserWithHistory10Message_whenGet_thenReturnMessageDtoListAndStatus200() {
    //given
    List<MessageDto> messages = Stream.iterate(1, i -> i + 1)
        .limit(15)
        .map(i -> getDataMessage("message", i))
        .map(dataMessage -> service.createMessage(dataMessage))
        .collect(Collectors.toList());
    int size = messages.size();

    dataMessage = getDataMessage("history ", 10);
    requestHeaders.add(
        "Authorization",
        "Bearer_" + jwtUtil.generateToken(userDetails));
    HttpEntity<Object> requestEntity = new HttpEntity<>(dataMessage, requestHeaders);
    ParameterizedTypeReference<List<MessageDto>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {};
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/message")
        .build();

    //when
    ResponseEntity<List<MessageDto>> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.POST,
        requestEntity,
        parameterizedTypeReference
    );

    // then
    List<MessageDto> actualMessageList = response.getBody();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");
    int actualSize = actualMessageList.size();
    assertThat(actualSize).isEqualTo(10);
    assertThat(actualMessageList.get(0).getId()).isEqualTo(messages.get(size-1).getId());
    assertThat(actualMessageList.get(0).getMessage()).isEqualTo(messages.get(size-1).getMessage());
    assertThat(actualMessageList.get(actualSize - 1).getId()).isEqualTo(messages.get(size-10).getId());
    assertThat(actualMessageList.get(actualSize - 1).getMessage()).isEqualTo(messages.get(size-10).getMessage());
  }

  @Test
  public void givenIncorrectHeader_when_thenReturnStatus403() {
    //given
    dataMessage= getDataMessage("message", 1);
    requestHeaders.add(
        "Authorization",
        "Bearer token");
    HttpEntity<Object> requestEntity = new HttpEntity<>(dataMessage, requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/message")
        .build();

    //when
    ResponseEntity<MessageDto> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.POST,
        requestEntity,
        MessageDto.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
  }

  private DataMessage getDataMessage(String message, int i) {
    DataMessage dataMessage = new DataMessage();
    dataMessage.setName("user");
    dataMessage.setMessage(message + i);
    return dataMessage;
  }
}