package com.example.springappdemo.unitTest.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.springappdemo.converter.MessageConverter;
import com.example.springappdemo.model.dto.DataMessage;
import com.example.springappdemo.model.dto.MessageDto;
import com.example.springappdemo.model.entity.Message;
import com.example.springappdemo.model.entity.MyUser;
import com.example.springappdemo.repository.MessageRepository;
import com.example.springappdemo.repository.MyUserRepository;
import com.example.springappdemo.service.MessageService;
import com.example.springappdemo.service.MessageServiceImpl;
import java.util.Collections;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  private MessageService service;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private MessageConverter converter;

  @Mock
  private MyUserRepository userRepository;

  private MyUser myUser;

  @BeforeEach
  public void setUp() {
    service = Mockito.spy(new MessageServiceImpl(messageRepository, converter, userRepository));
    myUser = new MyUser();
  }

  @Test
  public void givenMessage_whenSave_thenMessageReturned() {
    //given
    DataMessage dataMessage = new DataMessage();
    dataMessage.setMessage("message");
    dataMessage.setName("user");
    myUser.setMessages(new HashSet<>());
    when(userRepository.findByName(anyString())).thenReturn(myUser);
    when(messageRepository.save(any(Message.class))).thenReturn(new Message());
    when(converter.toMessageDto(any(Message.class))).thenReturn(new MessageDto());

    //when
    service.createMessage(dataMessage);

    //then
    verify(userRepository, times(1)).findByName(anyString());
    verify(messageRepository, times(1)).save(any(Message.class));
    verify(converter, times(1)).toMessageDto(any(Message.class));
    verifyNoMoreInteractions(userRepository, converter, messageRepository);
  }

  @Test
  public void givenMessage_whenSave_thenMessageListReturned() {
    //given
    when(messageRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());
    when(converter.toMessageDtos(any())).thenReturn(Collections.singletonList(new MessageDto()));

    //when
    service.findLastMessages();

    //then
    verify(messageRepository, times(1)).findAll(any(PageRequest.class));
    verify(converter, times(1)).toMessageDtos(any());
    verifyNoMoreInteractions(converter, messageRepository);
  }
}