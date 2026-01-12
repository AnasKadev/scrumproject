package org.example.scrum.util;

import org.example.scrum.entities.User;
import org.example.scrum.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public Initializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws RuntimeException {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("User");
            admin.setUsername("admin");
            admin.setPwd("admin123");
            admin.setRole("ADMIN");

            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Users already exist, skipping admin creation.");
        }
    }
}
