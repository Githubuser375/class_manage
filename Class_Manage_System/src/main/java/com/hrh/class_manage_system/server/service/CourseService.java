package com.hrh.class_manage_system.server.service;

import com.hrh.class_manage_system.common.result.PageResult;
import com.hrh.class_manage_system.pojo.dto.CourseCreateDTO;
import com.hrh.class_manage_system.pojo.vo.ScheduleCalendarVO;
import java.time.LocalDate;
import java.util.List;

public interface CourseService {
    // 分页查询 (替换了原来的 list 和 search)
    PageResult pageList(Integer page, Integer pageSize, String name);
    
    // 创建课程 (元数据)
    void createCourse(CourseCreateDTO dto);
    
    // 获取日历排期 (新增)
    List<ScheduleCalendarVO> getCourseSchedules(Integer courseId, LocalDate startDate, LocalDate endDate);

    // 删除课程
    Integer deleteCourse(Integer id);

    // 更新课程
    void updateCourse(CourseCreateDTO dto);
}