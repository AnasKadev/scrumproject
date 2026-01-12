package org.example.scrum.service;

import org.example.scrum.dto.AssignUserToProjectRequest;
import org.example.scrum.dto.ProjectUserDTO;
import org.example.scrum.entities.Project;
import org.example.scrum.entities.ProjectUser;
import org.example.scrum.entities.User;
import org.example.scrum.entities.enums.UserRole;
import org.example.scrum.exception.DuplicateResourceException;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.ProjectRepository;
import org.example.scrum.repository.ProjectUserRepository;
import org.example.scrum.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectUserServiceTest {

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectUserService projectUserService;

    private User testUser;
    private Project testProject;
    private ProjectUser testProjectUser;
    private AssignUserToProjectRequest assignRequest;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setUsername("jdoe");
        testUser.setEmail("jdoe@example.com");
        testUser.setRole(UserRole.DEVELOPER);
        testUser.setActive(true);

        // Créer un projet de test
        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("E-Commerce Platform");
        testProject.setDescription("Test project");
        testProject.setActive(true);

        // Créer un ProjectUser de test
        testProjectUser = new ProjectUser();
        testProjectUser.setId(1L);
        testProjectUser.setUser(testUser);
        testProjectUser.setProject(testProject);
        testProjectUser.setRole(UserRole.DEVELOPER);
        testProjectUser.setJoinedDate(LocalDate.now());
        testProjectUser.setActive(true);

        // Créer une requête d'assignement
        assignRequest = new AssignUserToProjectRequest();
        assignRequest.setUserId(1L);
        assignRequest.setProjectId(1L);
        assignRequest.setRole("DEVELOPER");
    }

    @Test
    void assignUserToProject_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectUserRepository.existsByUserIdAndProjectIdAndIsActiveTrue(1L, 1L)).thenReturn(false);
        when(projectUserRepository.save(any(ProjectUser.class))).thenReturn(testProjectUser);

        // Act
        ProjectUserDTO result = projectUserService.assignUserToProject(assignRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getProjectId());
        assertEquals("jdoe", result.getUsername());
        assertEquals("John Doe", result.getUserFullName());
        assertEquals("E-Commerce Platform", result.getProjectName());
        assertEquals(UserRole.DEVELOPER, result.getRole());
        assertTrue(result.isActive());
        verify(projectUserRepository, times(1)).save(any(ProjectUser.class));
    }

    @Test
    void assignUserToProject_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assignRequest.setUserId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.assignUserToProject(assignRequest);
        });
        verify(projectUserRepository, never()).save(any(ProjectUser.class));
    }

    @Test
    void assignUserToProject_ProjectNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());
        assignRequest.setProjectId(999L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.assignUserToProject(assignRequest);
        });
        verify(projectUserRepository, never()).save(any(ProjectUser.class));
    }

    @Test
    void assignUserToProject_DuplicateAssignment_ThrowsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectUserRepository.existsByUserIdAndProjectIdAndIsActiveTrue(1L, 1L)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            projectUserService.assignUserToProject(assignRequest);
        });
        verify(projectUserRepository, never()).save(any(ProjectUser.class));
    }

    @Test
    void getProjectMembers_Success() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectUserRepository.findByProjectIdAndIsActiveTrue(1L))
                .thenReturn(Arrays.asList(testProjectUser));

        // Act
        List<ProjectUserDTO> results = projectUserService.getProjectMembers(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("jdoe", results.get(0).getUsername());
        assertTrue(results.get(0).isActive());
    }

    @Test
    void getProjectMembers_ProjectNotFound_ThrowsException() {
        // Arrange
        when(projectRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.getProjectMembers(999L);
        });
    }

    @Test
    void getUserProjects_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(projectUserRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(testProjectUser));

        // Act
        List<ProjectUserDTO> results = projectUserService.getUserProjects(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("E-Commerce Platform", results.get(0).getProjectName());
        assertTrue(results.get(0).isActive());
    }

    @Test
    void getUserProjects_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.getUserProjects(999L);
        });
    }

    @Test
    void removeUserFromProject_Success() {
        // Arrange
        when(projectUserRepository.findByUserIdAndProjectId(1L, 1L))
                .thenReturn(Optional.of(testProjectUser));
        when(projectUserRepository.save(any(ProjectUser.class))).thenReturn(testProjectUser);

        // Act
        projectUserService.removeUserFromProject(1L, 1L);

        // Assert
        verify(projectUserRepository, times(1)).save(argThat(pu -> !pu.isActive()));
    }

    @Test
    void removeUserFromProject_NotFound_ThrowsException() {
        // Arrange
        when(projectUserRepository.findByUserIdAndProjectId(999L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.removeUserFromProject(999L, 1L);
        });
    }

    @Test
    void updateUserRole_Success() {
        // Arrange
        when(projectUserRepository.findByUserIdAndProjectId(1L, 1L))
                .thenReturn(Optional.of(testProjectUser));
        when(projectUserRepository.save(any(ProjectUser.class))).thenReturn(testProjectUser);

        // Act
        ProjectUserDTO result = projectUserService.updateUserRole(1L, 1L, "SCRUM_MASTER");

        // Assert
        assertNotNull(result);
        verify(projectUserRepository, times(1)).save(argThat(pu ->
            pu.getRole() == UserRole.SCRUM_MASTER
        ));
    }

    @Test
    void updateUserRole_NotFound_ThrowsException() {
        // Arrange
        when(projectUserRepository.findByUserIdAndProjectId(999L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.updateUserRole(999L, 1L, "SCRUM_MASTER");
        });
    }

    @Test
    void countProjectMembers_Success() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectUserRepository.countActiveMembers(1L)).thenReturn(3L);

        // Act
        Long count = projectUserService.countProjectMembers(1L);

        // Assert
        assertEquals(3L, count);
    }

    @Test
    void countProjectMembers_ProjectNotFound_ThrowsException() {
        // Arrange
        when(projectRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectUserService.countProjectMembers(999L);
        });
    }

    @Test
    void getProjectMembersByRole_Success() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectUserRepository.findByProjectIdAndRole(1L, UserRole.DEVELOPER))
                .thenReturn(Arrays.asList(testProjectUser));

        // Act
        List<ProjectUserDTO> results = projectUserService.getProjectMembersByRole(1L, "DEVELOPER");

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(UserRole.DEVELOPER, results.get(0).getRole());
    }

    @Test
    void isUserMemberOfProject_True() {
        // Arrange
        when(projectUserRepository.existsByUserIdAndProjectIdAndIsActiveTrue(1L, 1L))
                .thenReturn(true);

        // Act
        boolean result = projectUserService.isUserMemberOfProject(1L, 1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void isUserMemberOfProject_False() {
        // Arrange
        when(projectUserRepository.existsByUserIdAndProjectIdAndIsActiveTrue(1L, 1L))
                .thenReturn(false);

        // Act
        boolean result = projectUserService.isUserMemberOfProject(1L, 1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void getUserRoleInProject_Success() {
        // Arrange
        when(projectUserRepository.findByUserIdAndProjectId(1L, 1L))
                .thenReturn(Optional.of(testProjectUser));

        // Act
        UserRole result = projectUserService.getUserRoleInProject(1L, 1L);

        // Assert
        assertEquals(UserRole.DEVELOPER, result);
    }

    @Test
    void getUserRoleInProject_NotFound_ReturnsNull() {
        // Arrange
        when(projectUserRepository.findByUserIdAndProjectId(999L, 1L))
                .thenReturn(Optional.empty());

        // Act
        UserRole result = projectUserService.getUserRoleInProject(999L, 1L);

        // Assert
        assertNull(result);
    }

    @Test
    void reactivateUserInProject_Success() {
        // Arrange
        testProjectUser.setActive(false);
        when(projectUserRepository.findByUserIdAndProjectId(1L, 1L))
                .thenReturn(Optional.of(testProjectUser));
        when(projectUserRepository.save(any(ProjectUser.class))).thenReturn(testProjectUser);

        // Act
        ProjectUserDTO result = projectUserService.reactivateUserInProject(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(projectUserRepository, times(1)).save(argThat(ProjectUser::isActive));
    }

    @Test
    void getDevelopers_Success() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        when(projectUserRepository.findByProjectIdAndRole(1L, UserRole.DEVELOPER))
                .thenReturn(Arrays.asList(testProjectUser));

        // Act
        List<ProjectUserDTO> results = projectUserService.getDevelopers(1L);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(UserRole.DEVELOPER, results.get(0).getRole());
    }
}

