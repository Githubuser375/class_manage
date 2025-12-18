package com.hrh.class_manage_system.server.controller;

import com.hrh.class_manage_system.common.result.PageResult;
import com.hrh.class_manage_system.common.result.Result;
import com.hrh.class_manage_system.pojo.dto.CourseCreateDTO;
import com.hrh.class_manage_system.pojo.vo.ScheduleCalendarVO;
import com.hrh.class_manage_system.server.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/class")
public class CourseController {

    @Resource private CourseService courseService;

    // API 1.1 课程列表（分页）
    @GetMapping
    public Result<PageResult> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer page_size,
                                   @RequestParam(required = false) String name) {
        return Result.success(courseService.pageList(page, page_size, name));
    }

    // API 1.2 创建课程元数据
    @PostMapping("/create")
    public Result<?> create(@RequestBody CourseCreateDTO dto) {
        courseService.createCourse(dto);
        return Result.success();
    }

    // API 1.3 课程日历视图
    @GetMapping("/{id}/schedules")
    public Result<List<ScheduleCalendarVO>> getSchedules(@PathVariable Integer id,
                                                         @RequestParam("start_date") LocalDate startDate,
                                                         @RequestParam("end_date") LocalDate endDate) {
        return Result.success(courseService.getCourseSchedules(id, startDate, endDate));
    }

    // API 1.4 删除课程
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam Integer id) {
        Integer res = courseService.deleteCourse(id);
        Result<String> success = Result.success();
        Result<String> failure = Result.error(null);
        success.setData("课程删除成功");
        failure.setData("课程删除失败，请先删除该课程的排课信息");
        if(res==0){
            return failure;
        }
        else{
            return success;
        }
    }
}