package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
    List<Epic> epics=new ArrayList<Epic>();
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
    List<UserStory> userStories=new ArrayList<UserStory>();


}
