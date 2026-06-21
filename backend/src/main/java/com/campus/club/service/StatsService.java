package com.campus.club.service;

import com.campus.club.common.BusinessException;
import com.campus.club.dto.ClubStatsVO;
import com.campus.club.dto.MonthlyStatsVO;
import com.campus.club.mapper.ActivityMapper;
import com.campus.club.mapper.CheckinMapper;
import com.campus.club.mapper.RegistrationMapper;
import com.campus.club.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class StatsService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private CheckinMapper checkinMapper;

    private static final Pattern MONTH_PATTERN = Pattern.compile("^\\d{4}-\\d{2}$");

    public MonthlyStatsVO getMonthlyStats(String month) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "只有管理员可以查看统计数据");
        }

        if (month == null || !MONTH_PATTERN.matcher(month).matches()) {
            throw new BusinessException("月份格式不正确，请使用YYYY-MM格式");
        }

        MonthlyStatsVO vo = new MonthlyStatsVO();
        vo.setMonth(month);

        Integer activityCount = activityMapper.countActivitiesByMonth(month);
        vo.setTotalActivities(activityCount != null ? activityCount : 0);

        Integer registrationCount = registrationMapper.countRegistrationsByMonth(month);
        vo.setTotalRegistrations(registrationCount != null ? registrationCount : 0);

        Integer checkinCount = checkinMapper.countCheckinsByMonth(month);
        vo.setTotalCheckins(checkinCount != null ? checkinCount : 0);

        vo.setTotalNotCheckins(vo.getTotalRegistrations() - vo.getTotalCheckins());

        List<Map<String, Object>> clubStatsMap = activityMapper.selectClubStatsByMonth(month);
        List<ClubStatsVO> clubStatsList = new ArrayList<>();
        if (clubStatsMap != null) {
            for (Map<String, Object> map : clubStatsMap) {
                ClubStatsVO clubVO = new ClubStatsVO();
                clubVO.setClubName(map.get("club_name") != null ? map.get("club_name").toString() : null);
                clubVO.setActivityCount(map.get("activity_count") != null ? ((Number) map.get("activity_count")).intValue() : 0);
                clubVO.setRegistrationCount(map.get("registration_count") != null ? ((Number) map.get("registration_count")).intValue() : 0);
                clubVO.setCheckinCount(map.get("checkin_count") != null ? ((Number) map.get("checkin_count")).intValue() : 0);
                clubStatsList.add(clubVO);
            }
        }
        vo.setClubStats(clubStatsList);

        return vo;
    }
}
