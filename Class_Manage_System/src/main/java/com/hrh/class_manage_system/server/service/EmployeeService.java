package com.hrh.class_manage_system.server.service;

import com.hrh.class_manage_system.common.result.PageResult;
import com.hrh.class_manage_system.pojo.dto.EmployeeCreateDTO;

public interface EmployeeService {
    // 分页查询 (替换了原来的 list, search, chooseEmployees)
    PageResult pageList(Integer page, Integer pageSize, String name);
    
    // 创建员工
    void create(EmployeeCreateDTO dto);

    // 删除员工
    Integer delete(Integer id);
}