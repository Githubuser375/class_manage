package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleUpdateDTO {
    private Integer scheduleId;
    private LocalDate courseDate;
    private Integer startPeriod;
    private Integer endPeriod;
    private Integer classroomId;
}