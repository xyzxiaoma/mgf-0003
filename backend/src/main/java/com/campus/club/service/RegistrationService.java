package com.campus.club.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.club.common.BusinessException;
import com.campus.club.dto.RegistrationDetailVO;
import com.campus.club.entity.Activity;
import com.campus.club.entity.Registration;
import com.campus.club.entity.User;
import com.campus.club.mapper.ActivityMapper;
import com.campus.club.mapper.RegistrationMapper;
import com.campus.club.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserService userService;

    public Registration register(Long activityId) {
        Long userId = UserContext.getUserId();

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        if (!"NOT_STARTED".equals(activity.getStatus())) {
            throw new BusinessException("只有未开始的活动才能报名");
        }

        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId)
                .eq("user_id", userId)
                .eq("status", "REGISTERED");
        Registration existing = registrationMapper.selectOne(queryWrapper);
        if (existing != null) {
            throw new BusinessException("您已经报名过此活动");
        }

        Integer registeredCount = registrationMapper.countRegisteredByActivityId(activityId);
        if (registeredCount != null && registeredCount >= activity.getMaxCapacity()) {
            throw new BusinessException("活动报名人数已满");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);
        registration.setStudentName(user.getName());
        registration.setStudentNo(user.getStudentNo());
        registration.setPhone(user.getPhone());
        registration.setStatus("REGISTERED");
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setCreatedAt(LocalDateTime.now());
        registration.setUpdatedAt(LocalDateTime.now());

        registrationMapper.insert(registration);
        return registration;
    }

    public List<RegistrationDetailVO> getMyRegistrations() {
        Long userId = UserContext.getUserId();
        List<Map<String, Object>> results = registrationMapper.selectByUserIdWithActivity(userId);
        return convertRegistrationMapList(results);
    }

    public List<RegistrationDetailVO> getActivityRegistrations(Long activityId) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以查看报名人员");
        }

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        List<Map<String, Object>> results = registrationMapper.selectByActivityIdWithCheckin(activityId);
        return convertRegistrationMapList(results);
    }

    public void cancelRegistration(Long id) {
        Long userId = UserContext.getUserId();

        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        if (!registration.getUserId().equals(userId) && !UserContext.isAdmin()) {
            throw new BusinessException(403, "只能取消自己的报名");
        }

        if ("CANCELLED".equals(registration.getStatus())) {
            throw new BusinessException("该报名已取消");
        }

        registration.setStatus("CANCELLED");
        registration.setUpdatedAt(LocalDateTime.now());
        registrationMapper.updateById(registration);
    }

    public Registration getRegistrationById(Long id) {
        return registrationMapper.selectById(id);
    }

    private List<RegistrationDetailVO> convertRegistrationMapList(List<Map<String, Object>> results) {
        List<RegistrationDetailVO> voList = new ArrayList<>();
        if (results == null) {
            return voList;
        }
        for (Map<String, Object> map : results) {
            RegistrationDetailVO vo = new RegistrationDetailVO();
            vo.setId(map.get("id") != null ? ((Number) map.get("id")).longValue() : null);
            vo.setActivityId(map.get("activity_id") != null ? ((Number) map.get("activity_id")).longValue() : null);
            vo.setActivityName(map.get("activity_name") != null ? map.get("activity_name").toString() : null);
            vo.setUserId(map.get("user_id") != null ? ((Number) map.get("user_id")).longValue() : null);
            vo.setStudentName(map.get("student_name") != null ? map.get("student_name").toString() : null);
            vo.setStudentNo(map.get("student_no") != null ? map.get("student_no").toString() : null);
            vo.setPhone(map.get("phone") != null ? map.get("phone").toString() : null);
            vo.setStatus(map.get("status") != null ? map.get("status").toString() : null);
            vo.setRegisteredAt(map.get("registered_at") != null ? (LocalDateTime) map.get("registered_at") : null);
            vo.setCheckedIn(map.get("checked_in") != null && ((Number) map.get("checked_in")).intValue() == 1);
            voList.add(vo);
        }
        return voList;
    }
}
