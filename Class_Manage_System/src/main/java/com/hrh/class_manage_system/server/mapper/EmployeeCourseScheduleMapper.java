package com.hrh.class_manage_system.server.mapper;

import com.hrh.class_manage_system.pojo.entity.EmployeeCourseScheduleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeCourseScheduleMapper {
    List<Integer> selectScheduleIdsByEmployee(Integer employeeId);
    void insert(EmployeeCourseScheduleEntity entity);
    int countByScheduleId(Integer scheduleId);
    void assignEmployees(Integer scheduleId, List<Integer> employeeIds);
}
