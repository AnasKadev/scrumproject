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

    List<UserStory> findByProductBacklogId(Long productBacklogId);

    List<UserStory> findBySprintBacklogId(Long sprintBacklogId);

    List<UserStory> findByEpicId(Long epicId);

    List<UserStory> findByStatus(UserStoryStatus status);

    List<UserStory> findByPriority(Priority priority);

    List<UserStory> findByProductBacklogIdAndStatus(Long productBacklogId, UserStoryStatus status);

    List<UserStory> findByProductBacklogIdAndSprintBacklogIsNull(Long productBacklogId);

    // Trier par ordre de priorité
    @Query("SELECT us FROM UserStory us WHERE us.productBacklog.id = :productBacklogId ORDER BY us.priorityOrder ASC, us.businessValue DESC")
    List<UserStory> findByProductBacklogIdOrderByPriority(@Param("productBacklogId") Long productBacklogId);

    List<UserStory> findByProductBacklogIdOrderByPriorityOrderAsc(Long productBacklogId);

}



