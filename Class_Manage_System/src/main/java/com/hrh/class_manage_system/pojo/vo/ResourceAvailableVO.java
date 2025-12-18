package com.hrh.class_manage_system.pojo.vo;

import com.hrh.class_manage_system.pojo.entity.ClassroomEntity;
import com.hrh.class_manage_system.pojo.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAvailableVO {
    private List<ClassroomEntity> availableClassrooms;
    private List<EmployeeEntity> availableEmployees;
}