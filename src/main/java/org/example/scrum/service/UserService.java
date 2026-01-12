package org.example.scrum.service;

import org.example.scrum.entities.User;
import org.example.scrum.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userepo;
    public UserService(UserRepository userepo) {
        this.userepo = userepo;
    }



    public User saveUser(User user) {
        return userepo.save(user);
    }

}
