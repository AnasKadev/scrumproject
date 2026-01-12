package org.example.scrum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String role; // ADMIN, PRODUCT_OWNER, SCRUM_MASTER, DEVELOPER
}

