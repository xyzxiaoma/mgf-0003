package com.campus.club.dto;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyStatsVO {
    private String month;
    private Integer totalActivities;
    private Integer totalRegistrations;
    private Integer totalCheckins;
    private Integer totalNotCheckins;
    private List<ClubStatsVO> clubStats;
}
