package com.example.springappdemo.converter;

import com.example.springappdemo.model.dto.MessageDto;
import com.example.springappdemo.model.entity.Message;
import java.util.List;

public interface MessageConverter {

  /**
   * Convert Message to MessageDto
   *
   * @param message entity from database
   * @return MessageDto filled with fields
   */
  MessageDto toMessageDto(Message message);

  /**
   * Convert List of Message to List of MessageDto
   *
   * @param messages list of entity from database
   * @return list of MessageDto filled with fields
   */
  List<MessageDto> toMessageDtos(List<Message> messages);

}
