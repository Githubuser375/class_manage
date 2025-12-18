package com.hrh.class_manage_system.server.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hrh.class_manage_system.common.result.PageResult;
import com.hrh.class_manage_system.pojo.dto.CourseCreateDTO;
import com.hrh.class_manage_system.pojo.entity.CourseEntity;
import com.hrh.class_manage_system.pojo.vo.CourseVO;
import com.hrh.class_manage_system.pojo.vo.ScheduleCalendarVO;
import com.hrh.class_manage_system.server.mapper.CourseMapper;
import com.hrh.class_manage_system.server.mapper.CourseScheduleMapper;
import com.hrh.class_manage_system.server.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Resource private CourseMapper courseMapper;
    @Resource private CourseScheduleMapper scheduleMapper;

    @Override
    public PageResult pageList(Integer page, Integer pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        List<CourseVO> list = courseMapper.selectCourseList(name); // 确保 Mapper 支持 name 传参
        PageInfo<CourseVO> info = new PageInfo<>(list);
        return new PageResult(info.getTotal(), info.getList());
    }

    @Override
    public void createCourse(CourseCreateDTO dto) {
        CourseEntity entity = new CourseEntity();
        BeanUtils.copyProperties(dto, entity);
        courseMapper.insert(entity);
    }

    @Override
    public List<ScheduleCalendarVO> getCourseSchedules(Integer courseId, LocalDate startDate, LocalDate endDate) {
        return scheduleMapper.selectByCourseAndDateRange(courseId, startDate, endDate);
    }

    @Override
    public Integer deleteCourse(Integer id) {
        // 删除课程前，可能需要先删除相关的排课记录，视业务需求而定
        return courseMapper.deleteById(id);
    }

    @Override
    public void updateCourse(CourseCreateDTO dto) {
        CourseEntity entity = new CourseEntity();
        BeanUtils.copyProperties(dto, entity);
        courseMapper.update(entity);
    }
}