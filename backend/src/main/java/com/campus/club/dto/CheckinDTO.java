package com.campus.club.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CheckinDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    private String remark;
}
