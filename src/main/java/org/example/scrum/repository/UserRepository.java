package org.example.scrum.repository;

import org.example.scrum.entities.SprintBacklog;
import org.example.scrum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

