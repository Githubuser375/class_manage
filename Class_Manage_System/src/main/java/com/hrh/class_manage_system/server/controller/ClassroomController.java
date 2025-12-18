// 文件: com.hrh.class_manage_system.server.controller.ClassroomController.java

package com.hrh.class_manage_system.server.controller;

import com.hrh.class_manage_system.common.result.Result;
import com.hrh.class_manage_system.pojo.vo.ClassroomVO;
import com.hrh.class_manage_system.server.service.ClassroomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClassroomController {

    @Resource
    private ClassroomService classroomService;

    // 接口路径: /api/classroom
    @GetMapping("/classroom")
    public Result<List<ClassroomVO>> listAllClassrooms() {
        List<ClassroomVO> classrooms = classroomService.getAllClassrooms();
        return Result.success(classrooms);
    }
}