package org.example.scrum.repository;

import org.example.scrum.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Trouver par nom
    Optional<Project> findByName(String name);

    List<Project> findByNameContainingIgnoreCase(String name);

    // Trouver les projets actifs
    List<Project> findByIsActiveTrue();

    // Trouver les projets d'un utilisateur
    @Query("SELECT DISTINCT p FROM Project p JOIN p.projectMembers pm WHERE pm.user.id = :userId AND p.isActive = true")
    List<Project> findActiveProjectsByUserId(@Param("userId") Long userId);
}


