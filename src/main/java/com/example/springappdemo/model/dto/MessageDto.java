package com.example.springappdemo.model.dto;

public class MessageDto {

  private Long id;

  private String message;

  private Long userId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "MessageDto{" +
        "id=" + id +
        ", message='" + message + '\'' +
        ", userId=" + userId +
        '}';
  }
}
