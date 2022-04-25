package com.example.springappdemo.model.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String message;

  private Instant creationTime;

  public void setId(Long id) {
    this.id = id;
  }

  public void setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private MyUser user;

  public Long getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Instant getCreationTime() {
    return creationTime;
  }

  public MyUser getUser() {
    return user;
  }

  public void setUser(MyUser user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message1 = (Message) o;
    return Objects.equals(id, message1.id) && Objects
        .equals(message, message1.message) && Objects
        .equals(creationTime, message1.creationTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, message, creationTime);
  }
}
