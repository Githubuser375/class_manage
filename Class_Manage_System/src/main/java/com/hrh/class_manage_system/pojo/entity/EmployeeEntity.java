package com.hrh.class_manage_system.pojo.entity;

import lombok.Data;

@Data
//@TableName("employees")
public class EmployeeEntity {
    private Integer id;
    private String name;
    private Integer categoryId;
    private String phone;
}