package com.campus.club.controller;

import com.campus.club.common.Result;
import com.campus.club.dto.MonthlyStatsVO;
import com.campus.club.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public Result<MonthlyStatsVO> getMonthlyStats(@RequestParam String month) {
        MonthlyStatsVO stats = statsService.getMonthlyStats(month);
        return Result.success(stats);
    }
}
