package com.example.springappdemo.service;

import com.example.springappdemo.model.dto.DataMessage;
import com.example.springappdemo.model.dto.MessageDto;
import java.util.List;

public interface MessageService {

  List<MessageDto> findLastMessages();

  MessageDto createMessage(DataMessage message);

  void deleteMessage(MessageDto messageDto);

}
