package com.campus.club.controller;

import com.campus.club.common.Result;
import com.campus.club.dto.CheckinDTO;
import com.campus.club.dto.CheckinStatusVO;
import com.campus.club.dto.RegistrationDetailVO;
import com.campus.club.entity.Checkin;
import com.campus.club.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities/{id}/checkins")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    @PostMapping
    public Result<Checkin> checkin(
            @PathVariable Long id,
            @Validated @RequestBody CheckinDTO dto) {
        Checkin checkin = checkinService.checkin(id, dto.getUserId(), dto.getRemark());
        return Result.success(checkin);
    }

    @GetMapping
    public Result<List<RegistrationDetailVO>> getActivityCheckins(@PathVariable Long id) {
        List<RegistrationDetailVO> checkins = checkinService.getActivityCheckins(id);
        return Result.success(checkins);
    }

    @GetMapping("/status")
    public Result<CheckinStatusVO> getCheckinStatus(@PathVariable Long id) {
        CheckinStatusVO status = checkinService.getCheckinStatus(id);
        return Result.success(status);
    }
}
