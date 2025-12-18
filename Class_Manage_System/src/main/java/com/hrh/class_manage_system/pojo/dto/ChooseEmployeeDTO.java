package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChooseEmployeeDTO {
    private Integer categoryId;
    private LocalDate courseDate;
    private Integer startPeriod;
    private Integer endPeriod;
}
