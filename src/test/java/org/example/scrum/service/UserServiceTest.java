package org.example.scrum.service;

import org.example.scrum.dto.CreateUserRequest;
import org.example.scrum.dto.UserDTO;
import org.example.scrum.entities.User;
import org.example.scrum.entities.enums.UserRole;
import org.example.scrum.exception.DuplicateResourceException;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstname("John");
        testUser.setLastname("Doe");
        testUser.setUsername("jdoe");
        testUser.setEmail("jdoe@example.com");
        testUser.setPwd("password123");
        testUser.setRole(UserRole.DEVELOPER);
        testUser.setActive(true);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstname("John");
        createUserRequest.setLastname("Doe");
        createUserRequest.setUsername("jdoe");
        createUserRequest.setEmail("jdoe@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setRole("DEVELOPER");
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserDTO result = userService.createUser(createUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstname());
        assertEquals("jdoe", result.getUsername());
        assertEquals(UserRole.DEVELOPER, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DuplicateUsername_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsername("jdoe")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(createUserRequest);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(createUserRequest);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        UserDTO result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("jdoe", result.getUsername());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstname("Jane");
        user2.setLastname("Smith");
        user2.setUsername("jsmith");
        user2.setEmail("jsmith@example.com");
        user2.setRole(UserRole.PRODUCT_OWNER);
        user2.setActive(true);

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        // Act
        List<UserDTO> results = userService.getAllUsers();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("jdoe", results.get(0).getUsername());
        assertEquals("jsmith", results.get(1).getUsername());
    }

    @Test
    void getActiveUsers_Success() {
        // Arrange
        when(userRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(testUser));

        // Act
        List<UserDTO> results = userService.getActiveUsers();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).isActive());
    }

    @Test
    void getUsersByRole_Success() {
        // Arrange
        when(userRepository.findByRole(UserRole.DEVELOPER)).thenReturn(Arrays.asList(testUser));

        // Act
        List<UserDTO> results = userService.getUsersByRole("DEVELOPER");

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(UserRole.DEVELOPER, results.get(0).getRole());
    }

    @Test
    void updateUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        createUserRequest.setLastname("Doe Updated");

        // Act
        UserDTO result = userService.updateUser(1L, createUserRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deactivateUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.deactivateUser(1L);

        // Assert
        verify(userRepository, times(1)).save(argThat(user -> !user.isActive()));
    }

    @Test
    void activateUser_Success() {
        // Arrange
        testUser.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.activateUser(1L);

        // Assert
        verify(userRepository, times(1)).save(argThat(User::isActive));
    }

    @Test
    void deleteUser_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void authenticate_Success() {
        // Arrange
        when(userRepository.findByUsernameAndPwd("jdoe", "password123"))
                .thenReturn(Optional.of(testUser));

        // Act
        UserDTO result = userService.authenticate("jdoe", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("jdoe", result.getUsername());
    }

    @Test
    void authenticate_InvalidCredentials_ThrowsException() {
        // Arrange
        when(userRepository.findByUsernameAndPwd("jdoe", "wrongpassword"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticate("jdoe", "wrongpassword");
        });
    }

    @Test
    void authenticate_InactiveUser_ThrowsException() {
        // Arrange
        testUser.setActive(false);
        when(userRepository.findByUsernameAndPwd("jdoe", "password123"))
                .thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticate("jdoe", "password123");
        });
    }
}

