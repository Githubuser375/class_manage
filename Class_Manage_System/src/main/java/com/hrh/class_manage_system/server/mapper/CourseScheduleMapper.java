package com.hrh.class_manage_system.server.mapper;

import com.hrh.class_manage_system.pojo.entity.CourseScheduleEntity;
import com.hrh.class_manage_system.pojo.vo.ScheduleCalendarVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CourseScheduleMapper {
    void insert(CourseScheduleEntity entity);
    
    // 冲突计数
    int countConflict(@Param("classroomId") Integer classroomId,
                      @Param("date") LocalDate date,
                      @Param("startPeriod") Integer startPeriod,
                      @Param("endPeriod") Integer endPeriod);

    // 日历查询
    List<ScheduleCalendarVO> selectByCourseAndDateRange(@Param("courseId") Integer courseId,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate);

    // 根据 ID 查询排课记录
    CourseScheduleEntity selectById(Integer id);
    
    void deleteById(Integer id);
}