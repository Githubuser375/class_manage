package com.hrh.class_manage_system.pojo.entity;

import lombok.Data;

@Data
//@TableName("employee_course_schedule")
public class EmployeeCourseScheduleEntity {
    private Integer id;
    private Integer employeeId;
    private Integer scheduleId;
}