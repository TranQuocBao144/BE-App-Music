package com.example.admin_service.service;


import com.example.admin_service.dto.projection.UserCountByMonth;
import com.example.admin_service.dto.request.UserStatusNotification;
import com.example.admin_service.dto.request.UserupdateRequest;
import com.example.admin_service.dto.response.StaticResponse;
import com.example.admin_service.dto.response.UserResponse;
import com.example.admin_service.entity.User;
import com.example.admin_service.enums.Role;
import com.example.admin_service.enums.Status;
import com.example.admin_service.mapper.AdminMapper;
import com.example.admin_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AdminService {
    UserRepository userRepository;
    AdminMapper adminMapper;
    UserStatusProducer producer;

    public List<UserResponse> getUsers(){
        log.info("getUsers");
        List<User> users = userRepository.findByRoleAndStatus(Role.artist,Status.pending);
        log.info("Users: {}", users);
        return users.stream()
                .map(adminMapper::toUserResponse)
                .toList();
    }

    public List<StaticResponse> getMonthlyUserStats(int year) {
        // 1. Xác định khoảng thời gian của cả năm để tận dụng index
        LocalDateTime startDate = Year.of(year).atDay(1).atStartOfDay();
        LocalDateTime endDate = startDate.plusYears(1).minusNanos(1); // Cuối ngày 31/12

        // 2. Gọi repository để lấy dữ liệu thô từ DB (chỉ 1 lần duy nhất)
        List<UserCountByMonth> dbResults = userRepository.countUsersByMonthInYear(startDate, endDate);
        log.info("dbResults: {}", dbResults);
        // 3. Chuyển kết quả từ DB sang một Map để dễ dàng tra cứu
        // Key: tháng (Integer), Value: số lượng user (Long)
        Map<Integer, Long> monthlyCounts = dbResults.stream()
                .collect(Collectors.toMap(UserCountByMonth::getMonth, UserCountByMonth::getUserCount));

        // 4. Tạo danh sách kết quả đủ 12 tháng
        // Dùng IntStream để lặp từ 1 đến 12, đảm bảo không bỏ sót tháng nào
        return IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    // Lấy số lượng user từ Map, nếu không có thì mặc định là 0
                    long count = monthlyCounts.getOrDefault(month, 0L);
                    return new StaticResponse(month, year, count);
                })
                .collect(Collectors.toList());
    }


    public long CountUser() {
        return userRepository.count();
    }

    public UserResponse updateUser(String userId, UserupdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        adminMapper.updateStatusUser(user,request);

        return adminMapper.toUserResponse(userRepository.save(user));
    }




    public UserResponse updateUserStatus(String userId, UserupdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info(" Payload nhận từ client: {}", request);
        log.info(" userId nhận được: {}", userId);
        adminMapper.updateStatusUser(user, request);
        User saved = userRepository.save(user);

        // build payload
        UserStatusNotification notification = UserStatusNotification.builder()
                .email(saved.getEmail())
                .status(request.getStatus())
                .build();
        log.info("payload{}", notification);
        // gửi lên Kafka
        producer.sendStatusNotification(notification);

        return adminMapper.toUserResponse(saved);
    }


    public long countUserActive(){
        return userRepository.countByStatus(Status.active);
    }
    public long countUserWait(){
        return userRepository.countByStatus(Status.pending);
    }

}
