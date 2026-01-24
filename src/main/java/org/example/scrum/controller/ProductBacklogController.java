package org.example.scrum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateProductBacklogRequest;
import org.example.scrum.dto.ProductBacklogDTO;
import org.example.scrum.service.ProductBacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;

    @PostMapping
    public ResponseEntity<ProductBacklogDTO> createProductBacklog(@Valid @RequestBody CreateProductBacklogRequest request) {
        ProductBacklogDTO created = productBacklogService.createProductBacklog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> updateProductBacklog(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductBacklogRequest request) {
        ProductBacklogDTO updated = productBacklogService.updateProductBacklog(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> getProductBacklogById(@PathVariable Long id) {
        ProductBacklogDTO productBacklog = productBacklogService.getProductBacklogById(id);
        return ResponseEntity.ok(productBacklog);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProductBacklogDTO> getProductBacklogByProjectId(@PathVariable Long projectId) {
        ProductBacklogDTO productBacklog = productBacklogService.getProductBacklogByProjectId(projectId);
        return ResponseEntity.ok(productBacklog);
    }

    @GetMapping
    public ResponseEntity<List<ProductBacklogDTO>> getAllProductBacklogs() {
        List<ProductBacklogDTO> productBacklogs = productBacklogService.getAllProductBacklogs();
        return ResponseEntity.ok(productBacklogs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBacklog(@PathVariable Long id) {
        productBacklogService.deleteProductBacklog(id);
        return ResponseEntity.noContent().build();
    }
}

