package com.campus.club.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.club.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    @Select("SELECT a.*, COUNT(r.id) as registered_count " +
            "FROM activities a " +
            "LEFT JOIN registrations r ON a.id = r.activity_id AND r.status = 'REGISTERED' AND r.deleted = 0 " +
            "WHERE a.deleted = 0 " +
            "GROUP BY a.id " +
            "ORDER BY a.activity_date DESC, a.created_at DESC")
    List<Activity> selectActivityListWithCount();

    @Select("SELECT a.*, COUNT(r.id) as registered_count " +
            "FROM activities a " +
            "LEFT JOIN registrations r ON a.id = r.activity_id AND r.status = 'REGISTERED' AND r.deleted = 0 " +
            "WHERE a.deleted = 0 AND a.activity_date = #{date} " +
            "GROUP BY a.id " +
            "ORDER BY a.created_at DESC")
    List<Activity> selectActivityListByDate(@Param("date") LocalDate date);

    @Select("SELECT a.*, COUNT(r.id) as registered_count " +
            "FROM activities a " +
            "LEFT JOIN registrations r ON a.id = r.activity_id AND r.status = 'REGISTERED' AND r.deleted = 0 " +
            "WHERE a.deleted = 0 AND a.id = #{id} " +
            "GROUP BY a.id")
    Activity selectActivityByIdWithCount(@Param("id") Long id);

    @Select("SELECT COUNT(DISTINCT a.id) " +
            "FROM activities a " +
            "WHERE a.deleted = 0 " +
            "AND DATE_FORMAT(a.activity_date, '%Y-%m') = #{month}")
    Integer countActivitiesByMonth(@Param("month") String month);

    @Select("SELECT a.club_name, " +
            "COUNT(DISTINCT a.id) as activity_count, " +
            "COUNT(DISTINCT r.id) as registration_count, " +
            "COUNT(DISTINCT c.id) as checkin_count " +
            "FROM activities a " +
            "LEFT JOIN registrations r ON a.id = r.activity_id AND r.status = 'REGISTERED' AND r.deleted = 0 " +
            "LEFT JOIN checkins c ON a.id = c.activity_id AND c.deleted = 0 " +
            "WHERE a.deleted = 0 AND DATE_FORMAT(a.activity_date, '%Y-%m') = #{month} " +
            "GROUP BY a.club_name")
    List<Map<String, Object>> selectClubStatsByMonth(@Param("month") String month);
}
