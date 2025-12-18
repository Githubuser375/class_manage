package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignEmployeeDTO {
    private Integer scheduleId;
    private List<Integer> employeeIds;
}
