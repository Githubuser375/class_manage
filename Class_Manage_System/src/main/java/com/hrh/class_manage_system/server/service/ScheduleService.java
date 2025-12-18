package com.hrh.class_manage_system.server.service;

import com.hrh.class_manage_system.pojo.dto.ResourceAvailableDTO;
import com.hrh.class_manage_system.pojo.dto.ScheduleAddDTO;
import com.hrh.class_manage_system.pojo.vo.ResourceAvailableVO;

public interface ScheduleService {
    ResourceAvailableVO getAvailableResources(ResourceAvailableDTO dto);
    void addSchedule(ScheduleAddDTO dto);
    void deleteSchedule(Integer scheduleId);
    void assignEmployees(Integer scheduleId, java.util.List<Integer> employeeIds);
}