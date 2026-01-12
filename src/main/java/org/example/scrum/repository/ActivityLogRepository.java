package org.example.scrum.repository;

import org.example.scrum.entities.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByProjectId(Long projectId);
    List<ActivityLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    List<ActivityLog> findByPerformedById(Long userId);
    List<ActivityLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}