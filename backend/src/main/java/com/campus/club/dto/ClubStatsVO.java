package com.campus.club.dto;

import lombok.Data;

@Data
public class ClubStatsVO {
    private String clubName;
    private Integer activityCount;
    private Integer registrationCount;
    private Integer checkinCount;
}
