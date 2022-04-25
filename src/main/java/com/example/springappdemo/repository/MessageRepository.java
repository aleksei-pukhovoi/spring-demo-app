package com.example.springappdemo.repository;

import com.example.springappdemo.model.entity.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
