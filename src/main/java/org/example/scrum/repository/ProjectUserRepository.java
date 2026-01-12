package org.example.scrum.repository;

import org.example.scrum.entities.ProjectUser;
import org.example.scrum.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    List<ProjectUser> findByProjectId(Long projectId);

    List<ProjectUser> findByUserId(Long userId);

    Optional<ProjectUser> findByUserIdAndProjectId(Long userId, Long projectId);

    List<ProjectUser> findByProjectIdAndRole(Long projectId, UserRole role);

    List<ProjectUser> findByProjectIdAndIsActiveTrue(Long projectId);

    boolean existsByUserIdAndProjectIdAndIsActiveTrue(Long userId, Long projectId);

    // Compter les membres d'un projet
    @Query("SELECT COUNT(pu) FROM ProjectUser pu WHERE pu.project.id = :projectId AND pu.isActive = true")
    Long countActiveMembers(@Param("projectId") Long projectId);
}


