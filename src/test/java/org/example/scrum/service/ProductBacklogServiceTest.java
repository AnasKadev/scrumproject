package org.example.scrum.service;

import org.example.scrum.dto.CreateProductBacklogRequest;
import org.example.scrum.dto.ProductBacklogDTO;
import org.example.scrum.entities.ProductBacklog;
import org.example.scrum.entities.Project;
import org.example.scrum.exception.DuplicateResourceException;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.ProductBacklogMapper;
import org.example.scrum.repository.ProductBacklogRepository;
import org.example.scrum.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @InjectMocks
    private ProductBacklogService productBacklogService;

    private ProductBacklog testProductBacklog;
    private ProductBacklogDTO testProductBacklogDTO;
    private Project testProject;
    private CreateProductBacklogRequest createRequest;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");

        testProductBacklog = new ProductBacklog();
        testProductBacklog.setId(1L);
        testProductBacklog.setNom("Test Backlog");
        testProductBacklog.setDescription("Test Description");
        testProductBacklog.setProject(testProject);

        testProductBacklogDTO = new ProductBacklogDTO();
        testProductBacklogDTO.setId(1L);
        testProductBacklogDTO.setNom("Test Backlog");
        testProductBacklogDTO.setDescription("Test Description");
        testProductBacklogDTO.setProjectId(1L);

        createRequest = new CreateProductBacklogRequest();
        createRequest.setNom("Test Backlog");
        createRequest.setDescription("Test Description");
        createRequest.setProjectId(1L);
    }

    @Test
    void createProductBacklog_Success() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(testProductBacklog);
        when(productBacklogMapper.toDTO(any(ProductBacklog.class))).thenReturn(testProductBacklogDTO);

        // Act
        ProductBacklogDTO result = productBacklogService.createProductBacklog(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Backlog", result.getNom());
        verify(productBacklogRepository, times(1)).save(any(ProductBacklog.class));
    }

    @Test
    void createProductBacklog_ProjectNotFound_ThrowsException() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());
        createRequest.setProjectId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productBacklogService.createProductBacklog(createRequest);
        });
    }

    @Test
    void createProductBacklog_DuplicateBacklog_ThrowsException() {
        // Arrange
        testProject.setProductBacklog(testProductBacklog);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            productBacklogService.createProductBacklog(createRequest);
        });
    }

    @Test
    void updateProductBacklog_Success() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(testProductBacklog));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(testProductBacklog);
        when(productBacklogMapper.toDTO(any(ProductBacklog.class))).thenReturn(testProductBacklogDTO);

        // Act
        ProductBacklogDTO result = productBacklogService.updateProductBacklog(1L, createRequest);

        // Assert
        assertNotNull(result);
        verify(productBacklogRepository, times(1)).save(any(ProductBacklog.class));
    }

    @Test
    void getProductBacklogById_Success() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(testProductBacklog));
        when(productBacklogMapper.toDTO(any(ProductBacklog.class))).thenReturn(testProductBacklogDTO);

        // Act
        ProductBacklogDTO result = productBacklogService.getProductBacklogById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getProductBacklogByProjectId_Success() {
        // Arrange
        when(productBacklogRepository.findByProjectId(1L)).thenReturn(Optional.of(testProductBacklog));
        when(productBacklogMapper.toDTO(any(ProductBacklog.class))).thenReturn(testProductBacklogDTO);

        // Act
        ProductBacklogDTO result = productBacklogService.getProductBacklogByProjectId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getProjectId());
    }

    @Test
    void getAllProductBacklogs_Success() {
        // Arrange
        List<ProductBacklog> backlogs = Arrays.asList(testProductBacklog);
        List<ProductBacklogDTO> backlogDTOs = Arrays.asList(testProductBacklogDTO);
        when(productBacklogRepository.findAll()).thenReturn(backlogs);
        when(productBacklogMapper.toDTOList(backlogs)).thenReturn(backlogDTOs);

        // Act
        List<ProductBacklogDTO> results = productBacklogService.getAllProductBacklogs();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void deleteProductBacklog_Success() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(testProductBacklog));
        doNothing().when(productBacklogRepository).delete(any(ProductBacklog.class));

        // Act
        productBacklogService.deleteProductBacklog(1L);

        // Assert
        verify(productBacklogRepository, times(1)).delete(any(ProductBacklog.class));
    }
}

