package org.example.scrum.repository;

import org.example.scrum.entities.UserStory;
import org.example.scrum.entities.enums.Priority;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {

    // Trouver par Product Backlog
    List<UserStory> findByProductBacklogId(Long productBacklogId);

    // Trouver par Sprint Backlog
    List<UserStory> findBySprintBacklogId(Long sprintBacklogId);

    // Trouver par Epic
    List<UserStory> findByEpicId(Long epicId);

    // Trouver par statut
    List<UserStory> findByStatus(UserStoryStatus status);

    // Trouver par priorité
    List<UserStory> findByPriority(Priority priority);

    // Trouver par Product Backlog et statut
    List<UserStory> findByProductBacklogIdAndStatus(Long productBacklogId, UserStoryStatus status);

    // User Stories non assignées à un sprint
    List<UserStory> findByProductBacklogIdAndSprintBacklogIsNull(Long productBacklogId);

    // Trier par ordre de priorité
    @Query("SELECT us FROM UserStory us WHERE us.productBacklog.id = :productBacklogId ORDER BY us.priorityOrder ASC, us.businessValue DESC")
    List<UserStory> findByProductBacklogIdOrderByPriority(@Param("productBacklogId") Long productBacklogId);

    // Calculer la vélocité d'un sprint
    @Query("SELECT SUM(us.storyPoints) FROM UserStory us WHERE us.sprintBacklog.id = :sprintBacklogId AND us.status = 'DONE'")
    Integer calculateSprintVelocity(@Param("sprintBacklogId") Long sprintBacklogId);
}



