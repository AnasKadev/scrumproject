package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "scrum")
public class Project extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    private boolean isActive = true;

    // Un projet a UN product backlog
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private ProductBacklog productBacklog;

    // Un projet a plusieurs sprints
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<SprintBacklog> sprints = new ArrayList<>();

    // Membres du projet
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectUser> projectMembers = new ArrayList<>();
}
