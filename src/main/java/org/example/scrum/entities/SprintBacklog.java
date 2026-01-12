package org.example.scrum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.scrum.entities.enums.SprintStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "scrum")
public class SprintBacklog extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Objectif du sprint
    @Column(name = "sprint_goal", columnDefinition = "TEXT")
    private String sprintGoal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SprintStatus status = SprintStatus.PLANNED;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Numéro du sprint
    @Column(name = "sprint_number")
    private Integer sprintNumber;

    // Vélocité planifiée
    @Column(name = "planned_velocity")
    private Integer plannedVelocity;

    // Vélocité réelle
    @Column(name = "actual_velocity")
    private Integer actualVelocity;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private List<UserStory> userStories = new ArrayList<>();

    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
}
