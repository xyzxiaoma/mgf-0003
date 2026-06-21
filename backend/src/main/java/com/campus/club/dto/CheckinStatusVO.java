package com.campus.club.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckinStatusVO {
    private Integer totalRegistered;
    private Integer checkedInCount;
    private Integer notCheckedInCount;
    private List<RegistrationDetailVO> checkedInList;
    private List<RegistrationDetailVO> notCheckedInList;
}
