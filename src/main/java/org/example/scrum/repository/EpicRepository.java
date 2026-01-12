package org.example.scrum.repository;

import org.example.scrum.entities.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

    List<Epic> findByProductBacklogId(Long productBacklogId);

    List<Epic> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT COUNT(us) FROM UserStory us WHERE us.epic.id = :epicId")
    Long countUserStoriesByEpic(@Param("epicId") Long epicId);

    @Query("SELECT COUNT(us) FROM UserStory us WHERE us.epic.id = :epicId AND us.status = 'DONE'")
    Long countCompletedUserStoriesByEpic(@Param("epicId") Long epicId);
}



