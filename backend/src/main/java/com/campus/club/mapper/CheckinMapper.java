package com.campus.club.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.club.entity.Checkin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CheckinMapper extends BaseMapper<Checkin> {

    @Select("SELECT COUNT(*) " +
            "FROM checkins c " +
            "INNER JOIN activities a ON c.activity_id = a.id " +
            "WHERE c.deleted = 0 " +
            "AND DATE_FORMAT(a.activity_date, '%Y-%m') = #{month}")
    Integer countCheckinsByMonth(@Param("month") String month);

    @Select("SELECT c.*, r.student_name, r.student_no, r.phone, a.name as activity_name " +
            "FROM checkins c " +
            "LEFT JOIN registrations r ON c.activity_id = r.activity_id AND c.user_id = r.user_id " +
            "LEFT JOIN activities a ON c.activity_id = a.id " +
            "WHERE c.activity_id = #{activityId} AND c.deleted = 0 " +
            "ORDER BY c.checkin_time ASC")
    List<Map<String, Object>> selectByActivityIdWithUser(@Param("activityId") Long activityId);
}
