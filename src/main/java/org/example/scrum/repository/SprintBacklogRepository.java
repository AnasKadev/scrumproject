package org.example.scrum.repository;

import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {

    // Trouver par projet
    List<SprintBacklog> findByProjectId(Long projectId);

    // Trouver par statut
    List<SprintBacklog> findByStatus(SprintStatus status);

    // Trouver par projet et statut
    List<SprintBacklog> findByProjectIdAndStatus(Long projectId, SprintStatus status);

    // Trouver le sprint actif d'un projet
    Optional<SprintBacklog> findByProjectIdAndStatus(Long projectId, SprintStatus status);

    // Trouver les sprints dans une période
    @Query("SELECT s FROM SprintBacklog s WHERE s.project.id = :projectId AND s.startDate >= :startDate AND s.endDate <= :endDate ORDER BY s.sprintNumber")
    List<SprintBacklog> findByProjectIdAndDateRange(@Param("projectId") Long projectId,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    // Trouver par projet ordonné par numéro de sprint
    List<SprintBacklog> findByProjectIdOrderBySprintNumberDesc(Long projectId);

    // Trouver le dernier sprint d'un projet
    @Query("SELECT s FROM SprintBacklog s WHERE s.project.id = :projectId ORDER BY s.sprintNumber DESC LIMIT 1")
    Optional<SprintBacklog> findLatestSprintByProject(@Param("projectId") Long projectId);
}




