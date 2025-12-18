package com.hrh.class_manage_system.server.mapper;

import com.hrh.class_manage_system.pojo.entity.CourseCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CourseCategoryMapper {
    void insert(CourseCategoryEntity entity);
    void update(CourseCategoryEntity entity);
    List<CourseCategoryEntity> selectAll();

    CourseCategoryEntity selectById(Integer id);
    BigDecimal selectBaseFeeByScheduleId(Integer scheduleId);
    Integer deleteById(Integer id);
}
