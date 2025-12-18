package com.hrh.class_manage_system.pojo.vo;

import lombok.Data;


@Data
public class CourseVO {
    private Integer id;
    private String name;
    private Integer categoryId;
    private String categoryName;
    private String description;
}