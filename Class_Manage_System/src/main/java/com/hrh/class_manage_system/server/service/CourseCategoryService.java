package com.hrh.class_manage_system.server.service;

import com.hrh.class_manage_system.pojo.entity.CourseCategoryEntity;

import java.util.List;

public interface CourseCategoryService {
    void update(CourseCategoryEntity entity);

    void create(CourseCategoryEntity entity);

    List<CourseCategoryEntity> list();

    CourseCategoryEntity getById(Integer id);

    Integer deleteById(Integer id);

}