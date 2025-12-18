package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;

@Data
public class EmployeeCreateDTO {
    private String name;
    private Integer categoryId;
    private String phone;
}