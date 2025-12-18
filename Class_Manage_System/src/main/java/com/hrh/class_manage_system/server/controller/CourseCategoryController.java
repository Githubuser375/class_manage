package com.hrh.class_manage_system.server.controller;

import com.hrh.class_manage_system.common.result.Result;
import com.hrh.class_manage_system.pojo.entity.CourseCategoryEntity;
import com.hrh.class_manage_system.server.service.CourseCategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CourseCategoryController {

    @Resource
    private CourseCategoryService categoryService;

    /**
     * 新增课程类型
     */
    @PostMapping("/create")
    public Result<?> create(@RequestBody CourseCategoryEntity entity) {
        categoryService.create(entity);
        return Result.success();
    }

    /**
     * 更新课程类型
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody CourseCategoryEntity entity) {
        categoryService.update(entity);
        return Result.success();
    }

    /**
     * 查询所有课程类型（前端下拉框用）
     */
    @GetMapping
    public Result<List<CourseCategoryEntity>> list() {
        return Result.success(categoryService.list());
    }

    /**
     * 根据 id 查询
     */
    @GetMapping("/{id}")
    public Result<CourseCategoryEntity> get(@PathVariable Integer id) {
        return Result.success(categoryService.getById(id));
    }

    /**
     * 根据 id 删除课程类型
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam Integer id) {
        Result<String> success = Result.success();
        Result<String> failure = Result.error(null);
        success.setData("课程类型删除成功");
        failure.setData("课程类型删除失败，请先删除该类型下的课程以及员工关联信息");
        if(categoryService.deleteById(id)==0){
            return failure;
        }
        else{
            return success;
        }
    }
}
