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

    // Trouver les membres d'un projet
    List<ProjectUser> findByProjectId(Long projectId);

    // Trouver les projets d'un utilisateur
    List<ProjectUser> findByUserId(Long userId);

    // Trouver par utilisateur et projet
    Optional<ProjectUser> findByUserIdAndProjectId(Long userId, Long projectId);

    // Trouver par projet et rôle
    List<ProjectUser> findByProjectIdAndRole(Long projectId, UserRole role);

    // Trouver les membres actifs d'un projet
    List<ProjectUser> findByProjectIdAndIsActiveTrue(Long projectId);

    // Vérifier si un utilisateur est membre d'un projet
    boolean existsByUserIdAndProjectIdAndIsActiveTrue(Long userId, Long projectId);

    // Compter les membres d'un projet
    @Query("SELECT COUNT(pu) FROM ProjectUser pu WHERE pu.project.id = :projectId AND pu.isActive = true")
    Long countActiveMembers(@Param("projectId") Long projectId);
}


