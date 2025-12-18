package com.hrh.class_manage_system.pojo.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleCalendarVO {
    private Integer id;
    private LocalDate date;
    private Integer startPeriod;
    private Integer endPeriod;
    private String classroomCode;
    private List<String> teacherNames;
    private List<Integer> teacherIds;
    private BigDecimal actualFee;
}