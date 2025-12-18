package com.hrh.class_manage_system.server.mapper;

import com.hrh.class_manage_system.pojo.entity.ClassroomEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClassroomMapper {
    ClassroomEntity selectById(Integer id);
    
    // 查询可用教室（排除冲突）
    List<ClassroomEntity> selectAvailable(
    @Param("date") LocalDate date, 
    @Param("startPeriod") int startPeriod, 
    @Param("endPeriod") int endPeriod
);

    List<ClassroomEntity> selectAll();
}