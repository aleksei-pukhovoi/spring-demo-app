package com.example.springappdemo.service;

import com.example.springappdemo.model.entity.MyUser;
import com.example.springappdemo.repository.MyUserRepository;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private final MyUserRepository myUserRepository;

  public UserService(MyUserRepository myUserRepository) {
    this.myUserRepository = myUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    MyUser myUser = myUserRepository.findByName(username);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if (Objects.isNull(myUser)) {
      throw new UsernameNotFoundException("Unknown user: " + username);
    }
    return new User(
        myUser.getName(),
        passwordEncoder.encode(myUser.getPassword()),
        new ArrayList<>()
    );
  }
}
