package com.hrh.class_manage_system.server.service.Impl;

import com.hrh.class_manage_system.pojo.dto.ResourceAvailableDTO;
import com.hrh.class_manage_system.pojo.dto.ScheduleAddDTO;
import com.hrh.class_manage_system.pojo.entity.*;
import com.hrh.class_manage_system.pojo.vo.ResourceAvailableVO;
import com.hrh.class_manage_system.server.mapper.*;
import com.hrh.class_manage_system.server.service.ScheduleService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import com.hrh.class_manage_system.common.exception.ServiceException;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource private ClassroomMapper classroomMapper;
    @Resource private EmployeeMapper employeeMapper;
    @Resource private CourseScheduleMapper scheduleMapper;
    @Resource private EmployeeCourseScheduleMapper ecsMapper;
    @Resource private CourseMapper courseMapper;
    @Resource private CourseCategoryMapper categoryMapper;

    @Override
    public ResourceAvailableVO getAvailableResources(ResourceAvailableDTO dto) {
        List<ClassroomEntity> classrooms = classroomMapper.selectAvailable(
                dto.getDate(), dto.getStartPeriod(), dto.getEndPeriod());
        List<EmployeeEntity> employees = employeeMapper.selectAvailable(
                dto.getCategoryId(), dto.getDate(), dto.getStartPeriod(), dto.getEndPeriod());
        return new ResourceAvailableVO(classrooms, employees);
    }

@Transactional
    @Override
    public void addSchedule(ScheduleAddDTO dto) {
        // --- 调试日志开始 ---
        System.out.println("收到排课请求: " + dto);
        System.out.println("员工列表: " + dto.getEmployeeIds());
        // --- 调试日志结束 ---

        // 1. 安全检查
        int conflicts = scheduleMapper.countConflict(
                dto.getClassroomId(),
                dto.getDate(),
                dto.getStartPeriod(),
                dto.getEndPeriod()
        );
        if (conflicts > 0) {
            throw new ServiceException("所选教室已被占用，请刷新重试");
        }

        // 2. 计算费用
        // ！！！如果 CourseMapper.xml 缺 SQL，这里会报错！！！
        if(dto.getStartPeriod() > dto.getEndPeriod()) {
            throw new RuntimeException("结束节次必须大于等于开始节次");
        }

        if(dto.getEndPeriod()>=4&&dto.getStartPeriod()<=3||dto.getEndPeriod()>=7&&dto.getStartPeriod()<=6) {
            throw new RuntimeException("单次排课最多4节");
        }

        CourseEntity course = courseMapper.selectById(dto.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在 ID: " + dto.getCourseId());
        }

        CourseCategoryEntity category = categoryMapper.selectById(course.getCategoryId());
        if (category == null) {
            throw new RuntimeException("课程类型不存在 ID: " + course.getCategoryId());
        }

        // ！！！如果 JSON 映射失败，employeeIds 为空，这里会空指针！！！
        if (dto.getEmployeeIds() == null || dto.getEmployeeIds().isEmpty()) {
            throw new RuntimeException("必须选择至少一名老师");
        }

        BigDecimal baseFee = category.getBaseFee();
        int teacherCount = dto.getEmployeeIds().size();
        BigDecimal actualFee = baseFee.multiply(BigDecimal.valueOf(teacherCount));

        // 3. 插入排期
        CourseScheduleEntity schedule = new CourseScheduleEntity();
        BeanUtils.copyProperties(dto, schedule);
        schedule.setCourseDate(dto.getDate());
        schedule.setActualFee(actualFee);

        scheduleMapper.insert(schedule);

        // 4. 插入员工关联
        for (Integer empId : dto.getEmployeeIds()) {
            EmployeeCourseScheduleEntity ecs = new EmployeeCourseScheduleEntity();
            ecs.setScheduleId(schedule.getId());
            ecs.setEmployeeId(empId);
            ecsMapper.insert(ecs);
        }


    }

    @Override
    public void deleteSchedule(Integer scheduleId) {
        scheduleMapper.deleteById(scheduleId);
    }

    @Override
    public void assignEmployees(Integer scheduleId, List<Integer> employeeIds) {
        ecsMapper.assignEmployees(scheduleId, employeeIds);
    }

}
