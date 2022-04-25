package com.example.springappdemo.service;

import com.example.springappdemo.converter.MessageConverter;
import com.example.springappdemo.model.dto.DataMessage;
import com.example.springappdemo.model.dto.MessageDto;
import com.example.springappdemo.model.entity.Message;
import com.example.springappdemo.model.entity.MyUser;
import com.example.springappdemo.repository.MessageRepository;
import com.example.springappdemo.repository.MyUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService{

  private final MessageRepository messageRepository;
  private final MessageConverter converter;
  private final MyUserRepository userRepository;

  public MessageServiceImpl(MessageRepository messageRepository,
      MessageConverter converter, MyUserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.converter = converter;
    this.userRepository = userRepository;
  }

  @Override
  public List<MessageDto> findLastMessages() {
    Pageable pageRequest = PageRequest.of(0, 10, Sort.by("creationTime").descending());
    Page<Message> page = messageRepository.findAll(pageRequest);
    return converter.toMessageDtos(page.toList());
  }

  @Transactional
  @Override
  public MessageDto createMessage(DataMessage dataMessage) {
    Message message = new Message();
    message.setMessage(dataMessage.getMessage());
    message.setCreationTime(Instant.now());
    MyUser currentUser = userRepository.findByName(dataMessage.getName());
    Set<Message> messages = currentUser.getMessages();
    messages.add(message);
    message.setUser(currentUser);
    Message savedMessage = messageRepository.save(message);
    return converter.toMessageDto(savedMessage);
  }

  @Override
  public void deleteMessage(MessageDto messageDto) {
    messageRepository.deleteById(messageDto.getId());
  }
}
