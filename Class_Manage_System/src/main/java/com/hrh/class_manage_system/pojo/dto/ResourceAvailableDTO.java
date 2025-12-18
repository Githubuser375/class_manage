package com.hrh.class_manage_system.pojo.dto;

import lombok.Data;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ResourceAvailableDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;       // JSON: date
    private Integer startPeriod;  // JSON: start_period
    private Integer endPeriod;    // JSON: end_period
    private Integer categoryId;   // JSON: category_id (用于筛选老师)
}