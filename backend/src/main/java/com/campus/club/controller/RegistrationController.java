package com.campus.club.controller;

import com.campus.club.common.Result;
import com.campus.club.dto.RegistrationDetailVO;
import com.campus.club.entity.Registration;
import com.campus.club.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/activities/{id}/registrations")
    public Result<Registration> register(@PathVariable Long id) {
        Registration registration = registrationService.register(id);
        return Result.success(registration);
    }

    @GetMapping("/activities/{id}/registrations")
    public Result<List<RegistrationDetailVO>> getActivityRegistrations(@PathVariable Long id) {
        List<RegistrationDetailVO> registrations = registrationService.getActivityRegistrations(id);
        return Result.success(registrations);
    }

    @GetMapping("/users/me/registrations")
    public Result<List<RegistrationDetailVO>> getMyRegistrations() {
        List<RegistrationDetailVO> registrations = registrationService.getMyRegistrations();
        return Result.success(registrations);
    }

    @DeleteMapping("/registrations/{id}")
    public Result<?> cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
        return Result.success("取消报名成功");
    }
}
