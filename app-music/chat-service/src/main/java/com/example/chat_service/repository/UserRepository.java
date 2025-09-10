package com.example.chat_service.repository;

import com.example.chat_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * Provides methods to interact with user data
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find user by username
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find users by role
     * @param role the role to filter by (admin, artist)
     * @return List of users with the specified role
     */
    List<User> findByRole(String role);

    /**
     * Find active users by role
     * @param role the role to filter by
     * @param status the status to filter by (active)
     * @return List of active users with the specified role
     */
    List<User> findByRoleAndStatus(String role, String status);

    /**
     * Check if user exists and is active
     * @param userid the user ID to check
     * @return true if user exists and is active
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userid = :userid AND u.status = 'active'")
    boolean existsActiveUser(@Param("userid") Integer userid);
}
