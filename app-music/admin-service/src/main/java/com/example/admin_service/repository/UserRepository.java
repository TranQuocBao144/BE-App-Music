package com.example.admin_service.repository;

import com.example.admin_service.dto.projection.UserCountByMonth;
import com.example.admin_service.entity.User;
import com.example.admin_service.enums.Role;
import com.example.admin_service.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRoleAndStatus(Role role, Status status);
    Optional<User> findByUsername(String username);

    /**
     * Truy vấn để đếm số lượng người dùng được tạo trong mỗi tháng của một năm cụ thể.
     * - Dùng FUNCTION('MONTH', u.createat) để tương thích với JPQL chuẩn.
     * - Dùng WHERE u.createat BETWEEN :startDate AND :endDate để tận dụng index, giúp lọc nhanh hơn.
     * - Trả về một List các đối tượng UserCountByMonth.
     */
    @Query("SELECT new com.example.admin_service.dto.projection.UserCountByMonth(" +
            "MONTH(u.createat), COUNT(u.userid)) " +
            "FROM User u " +
            "WHERE u.createat BETWEEN :startDate AND :endDate " +
            "GROUP BY MONTH(u.createat) " +
            "ORDER BY MONTH(u.createat) ASC")
    List<UserCountByMonth> countUsersByMonthInYear(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);


    long countByStatus(Status status);
}
