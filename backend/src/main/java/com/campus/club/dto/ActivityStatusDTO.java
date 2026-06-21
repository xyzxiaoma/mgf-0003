package com.campus.club.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ActivityStatusDTO {
    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "^(NOT_STARTED|IN_PROGRESS|ENDED)$", message = "状态只能是NOT_STARTED、IN_PROGRESS或ENDED")
    private String status;
}
