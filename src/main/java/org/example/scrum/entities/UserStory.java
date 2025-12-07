package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String status;
    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
    private List<Task> tasks=new ArrayList<Task>();
    @ManyToOne
    private Epic epic;
    @ManyToOne
    private ProductBacklog productBacklog;
    @ManyToOne
    private SprintBacklog sprintBacklog;

}
