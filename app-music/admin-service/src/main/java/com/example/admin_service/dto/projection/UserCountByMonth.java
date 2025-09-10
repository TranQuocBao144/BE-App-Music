package com.example.admin_service.dto.projection;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCountByMonth {
    private Integer month;
    private Long userCount;

    public UserCountByMonth(Integer month, Long userCount) {
        this.month = month;
        this.userCount = userCount;
    }

    public Integer getMonth() {
        return month;
    }

    public Long getUserCount() {
        return userCount;
    }
}
