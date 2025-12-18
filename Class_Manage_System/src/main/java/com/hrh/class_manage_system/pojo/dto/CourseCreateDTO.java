package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;

@Data
public class CourseCreateDTO {
    private String name;
    private Integer categoryId;
    private String description;
    // 移除 location, date 等排课字段，排课现在是独立的流程
}