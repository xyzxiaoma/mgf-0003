package com.campus.club.controller;

import com.campus.club.common.Result;
import com.campus.club.dto.ActivityDTO;
import com.campus.club.dto.ActivityDetailVO;
import com.campus.club.dto.ActivityStatusDTO;
import com.campus.club.entity.Activity;
import com.campus.club.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public Result<List<ActivityDetailVO>> getAllActivities() {
        List<ActivityDetailVO> activities = activityService.getAllActivities();
        return Result.success(activities);
    }

    @GetMapping("/day")
    public Result<List<ActivityDetailVO>> getActivitiesByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<ActivityDetailVO> activities = activityService.getActivitiesByDate(date);
        return Result.success(activities);
    }

    @PostMapping
    public Result<Activity> createActivity(@Validated @RequestBody ActivityDTO dto) {
        Activity activity = activityService.createActivity(dto);
        return Result.success(activity);
    }

    @PutMapping("/{id}")
    public Result<Activity> updateActivity(
            @PathVariable Long id,
            @RequestBody ActivityDTO dto) {
        Activity activity = activityService.updateActivity(id, dto);
        return Result.success(activity);
    }

    @PatchMapping("/{id}/status")
    public Result<Activity> updateActivityStatus(
            @PathVariable Long id,
            @Validated @RequestBody ActivityStatusDTO dto) {
        Activity activity = activityService.updateActivityStatus(id, dto.getStatus());
        return Result.success(activity);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success("删除成功");
    }
}
