package com.hrh.class_manage_system.server.mapper;

import com.hrh.class_manage_system.pojo.entity.CourseEntity;
import com.hrh.class_manage_system.pojo.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseMapper {
    void insert(CourseEntity entity);
    
    CourseEntity selectById(Integer id); // 补充缺失的方法
    
    // 修改参数匹配 XML: 增加 @Param("name")
    List<CourseVO> selectCourseList(@Param("name") String name); 

    Integer deleteById(Integer id);

    void update(CourseEntity entity);
}