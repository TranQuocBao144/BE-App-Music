package com.example.admin_service.controller;


import com.example.admin_service.dto.ApiRespone;
import com.example.admin_service.dto.request.UserupdateRequest;
import com.example.admin_service.dto.response.StaticResponse;
import com.example.admin_service.dto.response.UserResponse;
import com.example.admin_service.mapper.AdminMapper;
import com.example.admin_service.repository.UserRepository;
import com.example.admin_service.service.AdminService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AdminController {
    AdminService adminService;

    @GetMapping
    ResponseEntity<List<UserResponse>> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PutMapping("/{userId}")
    public ApiRespone<UserResponse> updateUserStatus(@PathVariable String userId,
                                                     @Valid @RequestBody UserupdateRequest request) {
        try {
            ApiRespone<UserResponse> apiRespone = new ApiRespone<>();
            apiRespone.setResult(adminService.updateUserStatus(userId, request));
            return apiRespone;
        } catch (Exception e) {
            // Log error để debug
            log.error("Error updating user status: ", e);
            throw e;
        }
    }


    @GetMapping("/stats/monthly-registrations")
    public ResponseEntity<List<StaticResponse>> getMonthlyUserRegistrationStats(
            @RequestParam(value = "year", required = false) Integer year) {

        // Nếu không truyền năm, mặc định lấy năm hiện tại
        int targetYear = (year == null) ? Year.now().getValue() : year;

        List<StaticResponse> stats = adminService.getMonthlyUserStats(targetYear);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/count")
    public long countAllUsers() {
        return adminService.CountUser();
    }
    @GetMapping("/wait")
    public long countWaitUser() {
        return adminService.countUserWait();
    }
    @GetMapping("/done")
    public long countActiveUsers() {
        return adminService.countUserActive();
    }
}
