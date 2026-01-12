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

    List<SprintBacklog> findByProjectId(Long projectId);

    List<SprintBacklog> findByStatus(SprintStatus status);






}




