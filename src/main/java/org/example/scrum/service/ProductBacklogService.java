package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateProductBacklogRequest;
import org.example.scrum.dto.ProductBacklogDTO;
import org.example.scrum.entities.ProductBacklog;
import org.example.scrum.entities.Project;
import org.example.scrum.exception.DuplicateResourceException;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.ProductBacklogRepository;
import org.example.scrum.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductBacklogService {

    private final ProductBacklogRepository productBacklogRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public ProductBacklogDTO createProductBacklog(CreateProductBacklogRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + request.getProjectId()));

        if (project.getProductBacklog() != null) {
            throw new DuplicateResourceException("Ce projet possède déjà un Product Backlog");
        }

        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setNom(request.getNom());
        productBacklog.setDescription(request.getDescription());
        productBacklog.setProject(project);

        ProductBacklog saved = productBacklogRepository.save(productBacklog);
        return convertToDTO(saved);
    }

    @Transactional
    public ProductBacklogDTO updateProductBacklog(Long id, CreateProductBacklogRequest request) {
        ProductBacklog productBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog non trouvé avec l'ID: " + id));

        productBacklog.setNom(request.getNom());
        productBacklog.setDescription(request.getDescription());

        ProductBacklog updated = productBacklogRepository.save(productBacklog);
        return convertToDTO(updated);
    }

    @Transactional(readOnly = true)
    public ProductBacklogDTO getProductBacklogById(Long id) {
        ProductBacklog productBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog non trouvé avec l'ID: " + id));
        return convertToDTO(productBacklog);
    }

    @Transactional(readOnly = true)
    public ProductBacklogDTO getProductBacklogByProjectId(Long projectId) {
        ProductBacklog productBacklog = productBacklogRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog non trouvé pour le projet ID: " + projectId));
        return convertToDTO(productBacklog);
    }

    @Transactional(readOnly = true)
    public List<ProductBacklogDTO> getAllProductBacklogs() {
        return productBacklogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProductBacklog(Long id) {
        ProductBacklog productBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog non trouvé avec l'ID: " + id));
        productBacklogRepository.delete(productBacklog);
    }

    private ProductBacklogDTO convertToDTO(ProductBacklog productBacklog) {
        ProductBacklogDTO dto = new ProductBacklogDTO();
        dto.setId(productBacklog.getId());
        dto.setNom(productBacklog.getNom());
        dto.setDescription(productBacklog.getDescription());
        dto.setProjectId(productBacklog.getProject().getId());
        dto.setProjectName(productBacklog.getProject().getName());
        dto.setCreatedAt(productBacklog.getCreatedAt());
        dto.setUpdatedAt(productBacklog.getUpdatedAt());
        return dto;
    }
}

