package com.example.springappdemo.repository;

import com.example.springappdemo.model.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

  MyUser findByName(String name);
}
