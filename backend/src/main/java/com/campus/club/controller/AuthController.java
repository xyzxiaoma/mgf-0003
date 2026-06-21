package com.campus.club.controller;

import com.campus.club.common.Result;
import com.campus.club.dto.LoginDTO;
import com.campus.club.dto.LoginResponse;
import com.campus.club.dto.RegisterDTO;
import com.campus.club.dto.UserInfo;
import com.campus.club.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginDTO dto) {
        LoginResponse response = userService.login(dto);
        return Result.success(response);
    }

    @GetMapping("/me")
    public Result<UserInfo> getCurrentUser() {
        UserInfo userInfo = userService.getCurrentUser();
        return Result.success(userInfo);
    }
}
