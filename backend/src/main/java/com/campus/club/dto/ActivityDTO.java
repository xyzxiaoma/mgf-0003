package com.campus.club.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ActivityDTO {
    @NotBlank(message = "活动名称不能为空")
    private String name;

    @NotBlank(message = "所属社团不能为空")
    private String clubName;

    @NotNull(message = "活动日期不能为空")
    private LocalDate activityDate;

    @NotBlank(message = "活动地点不能为空")
    private String location;

    @NotNull(message = "人数上限不能为空")
    private Integer maxCapacity;

    private String description;

    private String status;
}
