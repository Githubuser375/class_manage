package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleAddDTO {
    private Integer courseId;
    private LocalDate date;
    private Integer startPeriod;
    private Integer endPeriod;
    private Integer classroomId;      // 必选：教室ID
    private List<Integer> employeeIds;// 必选：老师ID列表
}