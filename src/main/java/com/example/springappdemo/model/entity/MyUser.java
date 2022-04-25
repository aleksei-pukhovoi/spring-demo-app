package com.example.springappdemo.model.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class MyUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String password;

//  private String role;

  @OneToMany(mappedBy = "user")
  private Set<Message> messages;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Message> getMessages() {
    return messages;
  }

  public void setMessages(Set<Message> messages) {
    this.messages = messages;
  }

//  public String getRole() {
//    return role;
//  }
//
//  public void setRole(String role) {
//    this.role = role;
//  }
}
