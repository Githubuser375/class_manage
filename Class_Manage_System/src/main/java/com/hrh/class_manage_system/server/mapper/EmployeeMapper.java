package com.hrh.class_manage_system.server.mapper;

import com.hrh.class_manage_system.pojo.entity.EmployeeEntity;
import com.hrh.class_manage_system.pojo.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    void insert(EmployeeEntity entity);

    // 搜索列表
    List<EmployeeVO> selectList(@Param("name") String name);

    // 核心冲突检测 API 1.4
    List<EmployeeEntity> selectAvailable(
    @Param("categoryId") int categoryId,
    @Param("date") LocalDate date, 
    @Param("startPeriod") int startPeriod, 
    @Param("endPeriod") int endPeriod
    );

    Integer deleteById(Integer id);
}