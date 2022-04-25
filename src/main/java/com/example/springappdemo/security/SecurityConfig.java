package com.example.springappdemo.security;

import com.example.springappdemo.repository.MyUserRepository;
import com.example.springappdemo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JWTFilter jwtFilter;
  private final MyUserRepository repository;
  private final UserService userService;

//  public SecurityConfig(JWTFilter jwtFilter,
//      MyUserRepository repository) {
//    this.jwtFilter = jwtFilter;
//    this.repository = repository;
//  }


  public SecurityConfig(JWTFilter jwtFilter,
      MyUserRepository repository, UserService userService) {
    this.jwtFilter = jwtFilter;
    this.repository = repository;
    this.userService = userService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService);
//    auth.userDetailsService(username -> {
//          MyUser myUser = repository
//              .findByName(username);
//      if (Objects.isNull(myUser)) {
//        throw new UsernameNotFoundException("Unknown user: " + username);
//      }
//          return new User(
//              myUser.getName(),
//              passwordEncoder().encode(myUser.getPassword()),
//              new ArrayList<>()
//          );
//        }
//    );
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/authenticate").permitAll()
        .antMatchers("/message").authenticated()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
