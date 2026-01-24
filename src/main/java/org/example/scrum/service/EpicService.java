package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateEpicRequest;
import org.example.scrum.dto.EpicDTO;
import org.example.scrum.entities.Epic;
import org.example.scrum.entities.ProductBacklog;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.EpicRepository;
import org.example.scrum.repository.ProductBacklogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpicService {

    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;

    @Transactional
    public EpicDTO createEpic(CreateEpicRequest request) {
        ProductBacklog productBacklog = productBacklogRepository.findById(request.getProductBacklogId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog non trouvé avec l'ID: " + request.getProductBacklogId()));

        Epic epic = new Epic();
        epic.setTitle(request.getTitle());
        epic.setDescription(request.getDescription());
        epic.setColor(request.getColor());
        epic.setProductBacklog(productBacklog);

        Epic saved = epicRepository.save(epic);
        return convertToDTO(saved);
    }

    @Transactional
    public EpicDTO updateEpic(Long id, CreateEpicRequest request) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic non trouvé avec l'ID: " + id));

        epic.setTitle(request.getTitle());
        epic.setDescription(request.getDescription());
        epic.setColor(request.getColor());

        Epic updated = epicRepository.save(epic);
        return convertToDTO(updated);
    }

    @Transactional(readOnly = true)
    public EpicDTO getEpicById(Long id) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic non trouvé avec l'ID: " + id));
        return convertToDTO(epic);
    }

    @Transactional(readOnly = true)
    public List<EpicDTO> getAllEpics() {
        return epicRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EpicDTO> getEpicsByProductBacklogId(Long productBacklogId) {
        return epicRepository.findByProductBacklogId(productBacklogId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEpic(Long id) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic non trouvé avec l'ID: " + id));
        epicRepository.delete(epic);
    }

    private EpicDTO convertToDTO(Epic epic) {
        EpicDTO dto = new EpicDTO();
        dto.setId(epic.getId());
        dto.setTitle(epic.getTitle());
        dto.setDescription(epic.getDescription());
        dto.setColor(epic.getColor());
        dto.setProductBacklogId(epic.getProductBacklog().getId());
        dto.setProductBacklogName(epic.getProductBacklog().getNom());
        dto.setUserStoriesCount(epic.getUserStories() != null ? epic.getUserStories().size() : 0);
        dto.setCreatedAt(epic.getCreatedAt());
        dto.setUpdatedAt(epic.getUpdatedAt());
        return dto;
    }
}

