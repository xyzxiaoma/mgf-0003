package com.campus.club.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String name;
    private String studentNo;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
}
