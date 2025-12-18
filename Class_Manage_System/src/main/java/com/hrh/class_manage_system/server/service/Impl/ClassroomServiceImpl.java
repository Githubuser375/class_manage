package com.hrh.class_manage_system.server.service.Impl;

import com.hrh.class_manage_system.pojo.entity.ClassroomEntity;
import com.hrh.class_manage_system.pojo.vo.ClassroomVO;
import com.hrh.class_manage_system.server.mapper.ClassroomMapper;
import com.hrh.class_manage_system.server.service.ClassroomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Resource
    private ClassroomMapper classroomMapper;
    
    @Override
    public List<ClassroomVO> getAllClassrooms() {
        List<ClassroomEntity> entities = classroomMapper.selectAll();
        
        // 实体到 VO 的转换
        return entities.stream()
            .map(e -> {
                ClassroomVO vo = new ClassroomVO();
                BeanUtils.copyProperties(e, vo); 
                return vo;
            })
            .collect(Collectors.toList());
    }
}