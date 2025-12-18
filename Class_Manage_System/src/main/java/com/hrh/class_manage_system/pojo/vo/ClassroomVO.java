package com.hrh.class_manage_system.pojo.vo;

import lombok.Data; 
import java.io.Serializable;

@Data
public class ClassroomVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** 教室ID */
    private Integer id;
    
    /** 教室编码，例如 "101" */
    private String code;
}