package com.campus.club.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.club.common.BusinessException;
import com.campus.club.dto.CheckinStatusVO;
import com.campus.club.dto.RegistrationDetailVO;
import com.campus.club.entity.Activity;
import com.campus.club.entity.Checkin;
import com.campus.club.entity.Registration;
import com.campus.club.mapper.ActivityMapper;
import com.campus.club.mapper.CheckinMapper;
import com.campus.club.mapper.RegistrationMapper;
import com.campus.club.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckinService {

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private RegistrationService registrationService;

    public Checkin checkin(Long activityId, Long userId, String remark) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以进行签到");
        }

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        if (!"IN_PROGRESS".equals(activity.getStatus())) {
            throw new BusinessException("只有进行中的活动才能签到");
        }

        QueryWrapper<Registration> regQuery = new QueryWrapper<>();
        regQuery.eq("activity_id", activityId)
                .eq("user_id", userId)
                .eq("status", "REGISTERED");
        Registration registration = registrationMapper.selectOne(regQuery);
        if (registration == null) {
            throw new BusinessException("该学生未报名此活动，无法签到");
        }

        QueryWrapper<Checkin> checkinQuery = new QueryWrapper<>();
        checkinQuery.eq("activity_id", activityId)
                .eq("user_id", userId);
        Checkin existing = checkinMapper.selectOne(checkinQuery);
        if (existing != null) {
            throw new BusinessException("该学生已签到，不能重复签到");
        }

        Checkin checkin = new Checkin();
        checkin.setActivityId(activityId);
        checkin.setUserId(userId);
        checkin.setCheckinTime(LocalDateTime.now());
        checkin.setRemark(remark);
        checkin.setCreatedAt(LocalDateTime.now());
        checkin.setUpdatedAt(LocalDateTime.now());

        checkinMapper.insert(checkin);
        return checkin;
    }

    public List<RegistrationDetailVO> getActivityCheckins(Long activityId) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以查看签到记录");
        }

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        List<Map<String, Object>> results = checkinMapper.selectByActivityIdWithUser(activityId);
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
            vo.setRegisteredAt(map.get("checkin_time") != null ? (LocalDateTime) map.get("checkin_time") : null);
            vo.setCheckedIn(true);
            voList.add(vo);
        }
        return voList;
    }

    public CheckinStatusVO getCheckinStatus(Long activityId) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以查看签到状态");
        }

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        List<RegistrationDetailVO> allRegistrations = registrationService.getActivityRegistrations(activityId);

        List<RegistrationDetailVO> checkedInList = allRegistrations.stream()
                .filter(RegistrationDetailVO::getCheckedIn)
                .collect(Collectors.toList());

        List<RegistrationDetailVO> notCheckedInList = allRegistrations.stream()
                .filter(r -> !r.getCheckedIn())
                .collect(Collectors.toList());

        CheckinStatusVO vo = new CheckinStatusVO();
        vo.setTotalRegistered(allRegistrations.size());
        vo.setCheckedInCount(checkedInList.size());
        vo.setNotCheckedInCount(notCheckedInList.size());
        vo.setCheckedInList(checkedInList);
        vo.setNotCheckedInList(notCheckedInList);

        return vo;
    }
}
