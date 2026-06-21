package com.campus.club.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationDetailVO {
    private Long id;
    private Long activityId;
    private String activityName;
    private Long userId;
    private String studentName;
    private String studentNo;
    private String phone;
    private String status;
    private LocalDateTime registeredAt;
    private Boolean checkedIn;
}
