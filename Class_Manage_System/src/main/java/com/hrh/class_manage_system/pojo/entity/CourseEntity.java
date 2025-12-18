package com.hrh.class_manage_system.pojo.entity;

import lombok.Data;

@Data
//@TableName("courses")
public class CourseEntity {
    private Integer id;
    private String name;
    private Integer categoryId;
    private String description;
}