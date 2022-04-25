package com.example.springappdemo.converter;

import com.example.springappdemo.model.dto.MessageDto;
import com.example.springappdemo.model.entity.Message;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageConverterImpl implements MessageConverter{

  private final ModelMapper modelMapper;

  public MessageConverterImpl(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public MessageDto toMessageDto(Message message) {
    return modelMapper.map(message, MessageDto.class);
  }

  @Override
  public List<MessageDto> toMessageDtos(List<Message> messages) {
    return messages.stream()
        .map(this::toMessageDto)
        .collect(Collectors.toList());
  }
}
