package com.campus.club.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.club.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {

    @Select("SELECT COUNT(*) FROM registrations " +
            "WHERE activity_id = #{activityId} AND status = 'REGISTERED' AND deleted = 0")
    Integer countRegisteredByActivityId(@Param("activityId") Long activityId);

    @Select("SELECT r.*, a.name as activity_name " +
            "FROM registrations r " +
            "LEFT JOIN activities a ON r.activity_id = a.id " +
            "WHERE r.user_id = #{userId} AND r.deleted = 0 " +
            "ORDER BY r.registered_at DESC")
    List<Map<String, Object>> selectByUserIdWithActivity(@Param("userId") Long userId);

    @Select("SELECT r.* " +
            "FROM registrations r " +
            "WHERE r.activity_id = #{activityId} AND r.status = 'REGISTERED' AND r.deleted = 0 " +
            "ORDER BY r.registered_at ASC")
    List<Registration> selectByActivityId(@Param("activityId") Long activityId);

    @Select("SELECT COUNT(*) " +
            "FROM registrations r " +
            "INNER JOIN activities a ON r.activity_id = a.id " +
            "WHERE r.status = 'REGISTERED' AND r.deleted = 0 " +
            "AND DATE_FORMAT(a.activity_date, '%Y-%m') = #{month}")
    Integer countRegistrationsByMonth(@Param("month") String month);

    @Select("SELECT r.*, a.name as activity_name, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END as checked_in " +
            "FROM registrations r " +
            "LEFT JOIN activities a ON r.activity_id = a.id " +
            "LEFT JOIN checkins c ON r.activity_id = c.activity_id AND r.user_id = c.user_id AND c.deleted = 0 " +
            "WHERE r.activity_id = #{activityId} AND r.status = 'REGISTERED' AND r.deleted = 0 " +
            "ORDER BY r.registered_at ASC")
    List<Map<String, Object>> selectByActivityIdWithCheckin(@Param("activityId") Long activityId);
}
