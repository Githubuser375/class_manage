package com.hrh.class_manage_system.server.controller;

import com.hrh.class_manage_system.common.result.Result;
import com.hrh.class_manage_system.pojo.dto.ResourceAvailableDTO;
import com.hrh.class_manage_system.pojo.dto.ScheduleAddDTO;
import com.hrh.class_manage_system.pojo.vo.ResourceAvailableVO;
import com.hrh.class_manage_system.server.service.ScheduleService;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ScheduleController {

    @Resource private ScheduleService scheduleService;

    @PostMapping("/resource/available")
    public Result<ResourceAvailableVO> checkAvailable(@RequestBody ResourceAvailableDTO dto) {
        return Result.success(scheduleService.getAvailableResources(dto));
    }

    @PostMapping("/schedule/add")
    public Result<?> addSchedule(@RequestBody ScheduleAddDTO dto) {
        scheduleService.addSchedule(dto);
        return Result.success("排课成功");
    }

    @DeleteMapping("/schedule/delete")
    public Result<?> deleteSchedule(@RequestParam Integer scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return Result.success("排课删除成功");
    }

    @PostMapping("/schedule/assign-employees")
    public Result<?> assignEmployees(@RequestParam Integer scheduleId,
                                    @RequestBody List<Integer> employeeIds) {
        scheduleService.assignEmployees(scheduleId, employeeIds);
        return Result.success("员工分配成功");
    }
}