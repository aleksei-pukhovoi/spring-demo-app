package com.example.springappdemo.unitTest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springappdemo.controller.MessageController;
import com.example.springappdemo.model.dto.DataMessage;
import com.example.springappdemo.model.dto.MessageDto;
import com.example.springappdemo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

  private MockMvc mockMvc;

  private ObjectMapper mapper;

  @Mock
  private MessageService messageService;

  @InjectMocks
  private MessageController messageController;

  private MessageDto messageDto;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(messageController)
        .build();
    mapper = new ObjectMapper();
    messageDto = getMessageDto("message",1L);
  }

  @Test
  public void givenMessage_whenCreate_thenStatus201AndMessageReturned()
      throws Exception {
    //given
    when(messageService.createMessage(any(DataMessage.class))).thenReturn(messageDto);

    //when
    String expectedJsonResult = mapper.writeValueAsString(messageDto);
    MvcResult result = mockMvc.perform(post("/message")
        .contentType(MediaType.APPLICATION_JSON)
        .content(expectedJsonResult))

        //then
    .andExpect(status().isCreated())
        .andReturn();
    String actualJsonResponse = result.getResponse().getContentAsString();
    assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResult);
    verify(messageService, times(1)).createMessage(any((DataMessage.class)));
    verifyNoMoreInteractions(messageService);
  }

  @Test
  public void givenMessageHistory10_whenGet_thenStatus200AndMessageListReturned()
      throws Exception {
    //given
    DataMessage data = new DataMessage();
    data.setMessage("history 10");
    List<MessageDto> messageDtos = Collections.singletonList(messageDto);
    String expectedJsonResult = mapper.writeValueAsString(messageDtos);
    when(messageService.findLastMessages()).thenReturn(messageDtos);

    //when
    MvcResult result = mockMvc.perform(post("/message")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(data)))

        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();
    String actualJsonResponse = result.getResponse().getContentAsString();
    assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResult);
    verify(messageService, times(1)).findLastMessages();
    verifyNoMoreInteractions(messageService);
  }


  private MessageDto getMessageDto(String message, long i) {
    MessageDto messageDto = new MessageDto();
    messageDto.setId(i);
    messageDto.setMessage(message + i);
    messageDto.setUserId(1L);
    return messageDto;
  }
}