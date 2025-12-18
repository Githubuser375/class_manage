package com.hrh.class_manage_system.server.controller;

import com.hrh.class_manage_system.common.result.PageResult;
import com.hrh.class_manage_system.common.result.Result;
import com.hrh.class_manage_system.pojo.dto.EmployeeCreateDTO;
import com.hrh.class_manage_system.server.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * API 2.1 员工列表查询 (分页 + 搜索)
     * 整合了原来的 list 和 search 接口
     */
    @GetMapping
    public Result<PageResult> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer page_size,
                                   @RequestParam(required = false) String name) {
        // 注意：前端传参是 page_size，这里接收后传给 Service
        return Result.success(employeeService.pageList(page, page_size, name));
    }

    /**
     * API 2.2 创建员工
     */
    @PostMapping("/create")
    public Result<?> create(@RequestBody EmployeeCreateDTO dto) {
        employeeService.create(dto);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam Integer id) {
        Result<String> success = Result.success();
        Result<String> failure = Result.error(null);
        success.setData("员工删除成功");
        failure.setData("员工删除失败，请先删除该员工关联的排课信息");
        if(employeeService.delete(id)==0){
            return failure;
        }
        else{
            return success;
        }
    }

    
}