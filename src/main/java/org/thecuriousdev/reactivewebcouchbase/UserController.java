package org.thecuriousdev.reactivewebcouchbase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thecuriousdev.reactivewebcouchbase.domain.User;
import org.thecuriousdev.reactivewebcouchbase.repository.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/api/user/{name}")
  public Mono<User> findUserById(@PathVariable("name") String name) {
    return userService.findById(name);
  }

  @PostMapping("/api/user")
  public Mono<User> createUser(@RequestBody User user) {
    return userService.create(user);
  }

  @GetMapping("/api/user")
  public Flux<User> findAllUsers() {
    return userService.findAll();
  }

}
