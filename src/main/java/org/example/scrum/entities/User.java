package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.scrum.entities.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user", schema = "scrum")
public class User extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String firstname;

    @Column(nullable = false, length = 100)
    private String lastname;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String pwd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRole role;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ProjectUser> projectAssignments = new ArrayList<>();
}
