package org.example.scrum.repository;

import org.example.scrum.entities.ProductBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {
    Optional<ProductBacklog> findByNom(String nom);
}

