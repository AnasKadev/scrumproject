package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.CreateUserRequest;
import org.example.scrum.dto.UserDTO;
import org.example.scrum.entities.User;
import org.example.scrum.entities.enums.UserRole;
import org.example.scrum.exception.DuplicateResourceException;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        // verifier pas de meme username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Un utilisateur avec le username '" + request.getUsername() + "' existe deja");
        }

        // verifier l email
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Un utilisateur avec l email '" + request.getEmail() + "' existe deja");
        }

        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // TODO: hasher le mot de passe apres avec spring sec
        user.setPwd(request.getPassword());
        user.setRole(UserRole.valueOf(request.getRole()));
        user.setActive(true);

        User saved = userRepository.save(user);
        return convertToDTO(saved);
    }


    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        return convertToDTO(user);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<UserDTO> getActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<UserDTO> getUsersByRole(String role) {
        UserRole userRole = UserRole.valueOf(role);
        return userRepository.findByRole(userRole).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<UserDTO> searchUsersByName(String keyword) {
        return userRepository.searchByName(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public UserDTO updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));

        // Verifie le changement de l username
        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Un utilisateur avec le username '" + request.getUsername() + "' existe déjà");
        }

        // Vérifier le changement d email
        if (request.getEmail() != null
                && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Un utilisateur avec l'email '" + request.getEmail() + "' existe déjà");
        }

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPwd(request.getPassword());
        }
        user.setRole(UserRole.valueOf(request.getRole()));

        User updated = userRepository.save(user);
        return convertToDTO(updated);
    }


    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        user.setActive(false);
        userRepository.save(user);
    }


    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        user.setActive(true);
        userRepository.save(user);
    }


    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur", id);
        }
        userRepository.deleteById(id);
    }


    public UserDTO authenticate(String username, String password) {
        User user = userRepository.findByUsernameAndPwd(username, password)
                .orElseThrow(() -> new IllegalArgumentException("Username ou mot de passe incorrect"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Compte désactivé");
        }

        return convertToDTO(user);
    }


    public List<UserDTO> getAvailableDevelopers(int maxTasks) {
        return userRepository.findAvailableDevelopers(maxTasks).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }



}
