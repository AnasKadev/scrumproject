package org.example.scrum.controller;

import org.example.scrum.entities.User;
import org.example.scrum.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    UserService userservice;
    public UserController(UserService userservice){
        this.userservice=userservice;
    }
    @PostMapping("/creatUser")
    public void createUser(@RequestBody User user){
        userservice.saveUser(user);
    }
}
