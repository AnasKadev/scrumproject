package org.example.scrum.service;

import org.example.scrum.dto.CreateEpicRequest;
import org.example.scrum.dto.EpicDTO;
import org.example.scrum.entities.Epic;
import org.example.scrum.entities.ProductBacklog;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.mapper.EpicMapper;
import org.example.scrum.repository.EpicRepository;
import org.example.scrum.repository.ProductBacklogRepository;
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
class EpicServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicMapper epicMapper;

    @InjectMocks
    private EpicService epicService;

    private Epic testEpic;
    private EpicDTO testEpicDTO;
    private ProductBacklog testProductBacklog;
    private CreateEpicRequest createRequest;

    @BeforeEach
    void setUp() {
        testProductBacklog = new ProductBacklog();
        testProductBacklog.setId(1L);
        testProductBacklog.setNom("Test Backlog");

        testEpic = new Epic();
        testEpic.setId(1L);
        testEpic.setTitle("Test Epic");
        testEpic.setDescription("Test Description");
        testEpic.setColor("#FF0000");
        testEpic.setProductBacklog(testProductBacklog);

        testEpicDTO = new EpicDTO();
        testEpicDTO.setId(1L);
        testEpicDTO.setTitle("Test Epic");
        testEpicDTO.setDescription("Test Description");
        testEpicDTO.setColor("#FF0000");
        testEpicDTO.setProductBacklogId(1L);

        createRequest = new CreateEpicRequest();
        createRequest.setTitle("Test Epic");
        createRequest.setDescription("Test Description");
        createRequest.setColor("#FF0000");
        createRequest.setProductBacklogId(1L);
    }

    @Test
    void createEpic_Success() {
        // Arrange
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(testProductBacklog));
        when(epicRepository.save(any(Epic.class))).thenReturn(testEpic);
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(testEpicDTO);

        // Act
        EpicDTO result = epicService.createEpic(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Epic", result.getTitle());
        assertEquals("#FF0000", result.getColor());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    @Test
    void createEpic_ProductBacklogNotFound_ThrowsException() {
        // Arrange
        when(productBacklogRepository.findById(999L)).thenReturn(Optional.empty());
        createRequest.setProductBacklogId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            epicService.createEpic(createRequest);
        });
    }

    @Test
    void updateEpic_Success() {
        // Arrange
        when(epicRepository.findById(1L)).thenReturn(Optional.of(testEpic));
        when(epicRepository.save(any(Epic.class))).thenReturn(testEpic);
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(testEpicDTO);

        // Act
        EpicDTO result = epicService.updateEpic(1L, createRequest);

        // Assert
        assertNotNull(result);
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    @Test
    void updateEpic_NotFound_ThrowsException() {
        // Arrange
        when(epicRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            epicService.updateEpic(999L, createRequest);
        });
    }

    @Test
    void getEpicById_Success() {
        // Arrange
        when(epicRepository.findById(1L)).thenReturn(Optional.of(testEpic));
        when(epicMapper.toDTO(any(Epic.class))).thenReturn(testEpicDTO);

        // Act
        EpicDTO result = epicService.getEpicById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getEpicById_NotFound_ThrowsException() {
        // Arrange
        when(epicRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            epicService.getEpicById(999L);
        });
    }

    @Test
    void getAllEpics_Success() {
        // Arrange
        List<Epic> epics = Arrays.asList(testEpic);
        List<EpicDTO> epicDTOs = Arrays.asList(testEpicDTO);
        when(epicRepository.findAll()).thenReturn(epics);
        when(epicMapper.toDTOList(epics)).thenReturn(epicDTOs);

        // Act
        List<EpicDTO> results = epicService.getAllEpics();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getEpicsByProductBacklogId_Success() {
        // Arrange
        List<Epic> epics = Arrays.asList(testEpic);
        List<EpicDTO> epicDTOs = Arrays.asList(testEpicDTO);
        when(epicRepository.findByProductBacklogId(1L)).thenReturn(epics);
        when(epicMapper.toDTOList(epics)).thenReturn(epicDTOs);

        // Act
        List<EpicDTO> results = epicService.getEpicsByProductBacklogId(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void deleteEpic_Success() {
        // Arrange
        when(epicRepository.findById(1L)).thenReturn(Optional.of(testEpic));
        doNothing().when(epicRepository).delete(any(Epic.class));

        // Act
        epicService.deleteEpic(1L);

        // Assert
        verify(epicRepository, times(1)).delete(any(Epic.class));
    }

    @Test
    void deleteEpic_NotFound_ThrowsException() {
        // Arrange
        when(epicRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            epicService.deleteEpic(999L);
        });
    }
}

