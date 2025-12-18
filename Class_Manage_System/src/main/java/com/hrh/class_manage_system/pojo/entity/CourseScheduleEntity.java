package com.hrh.class_manage_system.pojo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CourseScheduleEntity {
    private Integer id;
    private Integer courseId;
    private Integer classroomId; // 修改点：关联 ID
    private LocalDate courseDate;
    private Integer startPeriod;
    private Integer endPeriod;
    private BigDecimal actualFee; // 新增：实际费用
}