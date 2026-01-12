package org.example.scrum.repository;

import org.example.scrum.entities.User;
import org.example.scrum.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndPwd(String username, String pwd);

    List<User> findByRole(UserRole role);

    List<User> findByIsActiveTrue();

    List<User> findByRoleAndIsActiveTrue(UserRole role, boolean isActive);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Recherche par nom
    @Query("SELECT u FROM User u WHERE LOWER(u.firstname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByName(@Param("keyword") String keyword);

    // Développeurs disponibles (non surchargés)
    @Query("SELECT u FROM User u WHERE u.role = 'DEVELOPER' AND u.isActive = true AND (SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = u.id AND t.status IN ('TO_DO', 'IN_PROGRESS')) < :maxTasks")
    List<User> findAvailableDevelopers(@Param("maxTasks") long maxTasks);
}



