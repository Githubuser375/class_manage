package com.hrh.class_manage_system.server.service.Impl;

import com.hrh.class_manage_system.pojo.entity.CourseCategoryEntity;
import com.hrh.class_manage_system.server.mapper.CourseCategoryMapper;
import com.hrh.class_manage_system.server.service.CourseCategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Resource
    private CourseCategoryMapper categoryMapper;

    @Override
    public void create(CourseCategoryEntity entity) {
        categoryMapper.insert(entity);
    }

    @Override
    public void update(CourseCategoryEntity entity) {
        categoryMapper.update(entity);
    }

    @Override
    public List<CourseCategoryEntity> list() {
        return categoryMapper.selectAll();
    }

    @Override
    public CourseCategoryEntity getById(Integer id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public Integer deleteById(Integer id) {
        return categoryMapper.deleteById(id);
    }
}

