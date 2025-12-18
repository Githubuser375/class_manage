package com.hrh.class_manage_system.server.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hrh.class_manage_system.common.result.PageResult;
import com.hrh.class_manage_system.pojo.dto.EmployeeCreateDTO;
import com.hrh.class_manage_system.pojo.entity.EmployeeEntity;
import com.hrh.class_manage_system.pojo.vo.EmployeeVO;
import com.hrh.class_manage_system.server.mapper.EmployeeMapper;
import com.hrh.class_manage_system.server.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public PageResult pageList(Integer page, Integer pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        List<EmployeeVO> list = employeeMapper.selectList(name);
        PageInfo<EmployeeVO> info = new PageInfo<>(list);
        return new PageResult(info.getTotal(), info.getList());
    }

    @Override
    public void create(EmployeeCreateDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        BeanUtils.copyProperties(dto, entity);
        employeeMapper.insert(entity);
    }

    @Override
    public Integer delete(Integer id) {
       return employeeMapper.deleteById(id);
    }
}