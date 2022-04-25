package com.example.springappdemo.controller;

import com.example.springappdemo.model.dto.DataMessage;
import com.example.springappdemo.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping(value="/message")
  public ResponseEntity<?> createMessage(@RequestBody DataMessage message) {
    if (message.getMessage().equals("history 10")) {
      return new ResponseEntity<>(messageService.findLastMessages(), HttpStatus.OK);
    }
    return new ResponseEntity<>(messageService.createMessage(message), HttpStatus.CREATED);
  }

}
