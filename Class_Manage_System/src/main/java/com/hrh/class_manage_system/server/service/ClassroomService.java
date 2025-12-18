package com.hrh.class_manage_system.server.service;

import com.hrh.class_manage_system.pojo.vo.ClassroomVO;
import java.util.List;

public interface ClassroomService {
    /** 获取所有教室列表 */
    List<ClassroomVO> getAllClassrooms(); 
}