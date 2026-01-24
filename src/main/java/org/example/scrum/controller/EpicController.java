package org.example.scrum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateEpicRequest;
import org.example.scrum.dto.EpicDTO;
import org.example.scrum.service.EpicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    @PostMapping
    public ResponseEntity<EpicDTO> createEpic(@Valid @RequestBody CreateEpicRequest request) {
        EpicDTO created = epicService.createEpic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpicDTO> updateEpic(
            @PathVariable Long id,
            @Valid @RequestBody CreateEpicRequest request) {
        EpicDTO updated = epicService.updateEpic(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpicDTO> getEpicById(@PathVariable Long id) {
        EpicDTO epic = epicService.getEpicById(id);
        return ResponseEntity.ok(epic);
    }

    @GetMapping
    public ResponseEntity<List<EpicDTO>> getAllEpics() {
        List<EpicDTO> epics = epicService.getAllEpics();
        return ResponseEntity.ok(epics);
    }

    @GetMapping("/product-backlog/{productBacklogId}")
    public ResponseEntity<List<EpicDTO>> getEpicsByProductBacklogId(@PathVariable Long productBacklogId) {
        List<EpicDTO> epics = epicService.getEpicsByProductBacklogId(productBacklogId);
        return ResponseEntity.ok(epics);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        epicService.deleteEpic(id);
        return ResponseEntity.noContent().build();
    }
}

