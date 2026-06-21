package com.campus.club.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.club.common.BusinessException;
import com.campus.club.dto.ActivityDTO;
import com.campus.club.dto.ActivityDetailVO;
import com.campus.club.entity.Activity;
import com.campus.club.mapper.ActivityMapper;
import com.campus.club.utils.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    public List<ActivityDetailVO> getAllActivities() {
        List<Activity> activities = activityMapper.selectActivityListWithCount();
        return convertToVOList(activities);
    }

    public List<ActivityDetailVO> getActivitiesByDate(LocalDate date) {
        List<Activity> activities = activityMapper.selectActivityListByDate(date);
        return convertToVOList(activities);
    }

    public ActivityDetailVO getActivityById(Long id) {
        Activity activity = activityMapper.selectActivityByIdWithCount(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        return convertToVO(activity);
    }

    public Activity createActivity(ActivityDTO dto) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以创建活动");
        }

        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        if (activity.getStatus() == null) {
            activity.setStatus("NOT_STARTED");
        }
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        activityMapper.insert(activity);
        return activity;
    }

    public Activity updateActivity(Long id, ActivityDTO dto) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以修改活动");
        }

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        if (dto.getName() != null) {
            activity.setName(dto.getName());
        }
        if (dto.getClubName() != null) {
            activity.setClubName(dto.getClubName());
        }
        if (dto.getActivityDate() != null) {
            activity.setActivityDate(dto.getActivityDate());
        }
        if (dto.getLocation() != null) {
            activity.setLocation(dto.getLocation());
        }
        if (dto.getMaxCapacity() != null) {
            activity.setMaxCapacity(dto.getMaxCapacity());
        }
        if (dto.getDescription() != null) {
            activity.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            activity.setStatus(dto.getStatus());
        }
        activity.setUpdatedAt(LocalDateTime.now());

        activityMapper.updateById(activity);
        return activity;
    }

    public Activity updateActivityStatus(Long id, String status) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以修改活动状态");
        }

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        activity.setStatus(status);
        activity.setUpdatedAt(LocalDateTime.now());
        activityMapper.updateById(activity);
        return activity;
    }

    public void deleteActivity(Long id) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以删除活动");
        }

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        activityMapper.deleteById(id);
    }

    private List<ActivityDetailVO> convertToVOList(List<Activity> activities) {
        if (activities == null) {
            return new ArrayList<>();
        }
        return activities.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private ActivityDetailVO convertToVO(Activity activity) {
        ActivityDetailVO vo = new ActivityDetailVO();
        BeanUtils.copyProperties(activity, vo);
        vo.setRegisteredCount(activity.getRegisteredCount() != null ? activity.getRegisteredCount() : 0);
        return vo;
    }
}
