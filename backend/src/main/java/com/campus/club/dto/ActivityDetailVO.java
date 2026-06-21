package com.campus.club.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ActivityDetailVO {
    private Long id;
    private String name;
    private String clubName;
    private LocalDate activityDate;
    private String location;
    private Integer maxCapacity;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer registeredCount;
}
