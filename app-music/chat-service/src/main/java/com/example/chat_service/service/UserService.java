package com.example.chat_service.service;
import com.example.chat_service.dto.UserDto;
import com.example.chat_service.entity.User;
import com.example.chat_service.enums.Role;
import com.example.chat_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for user-related operations
 * Handles user validation and role-based queries
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Find user by ID
     *
     * @param userId the user ID
     * @return Optional user DTO
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Integer userId) {
        return userRepository.findById(userId)
                .map(this::convertToDto);
    }

    /**
     * Find all active users by role
     *
     * @param role the role to filter by
     * @return list of active users with the specified role
     */
    @Transactional(readOnly = true)
    public List<UserDto> findActiveUsersByRole(Role role) {
        return userRepository.findByRoleAndStatus(role.name(), "active")
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Check if user exists and is active
     *
     * @param userId the user ID to check
     * @return true if user exists and is active
     */
    @Transactional(readOnly = true)
    public boolean isUserActive(Integer userId) {
        return userRepository.existsActiveUser(userId);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .userid(user.getUserid())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}