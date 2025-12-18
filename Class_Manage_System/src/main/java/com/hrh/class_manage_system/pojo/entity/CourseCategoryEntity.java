package com.hrh.class_manage_system.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
//@TableName("course_categories")
public class CourseCategoryEntity {
    private Integer id;
    private String name;
    private BigDecimal baseFee;
}