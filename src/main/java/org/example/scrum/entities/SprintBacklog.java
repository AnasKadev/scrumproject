package org.example.scrum.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private List<UserStory> userStories=new ArrayList<UserStory>();
    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private  List<Task> tasks=new ArrayList<Task>();
}
