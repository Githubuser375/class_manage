// 文件: com.hrh.class_manage_system.common.handler.GlobalExceptionHandler.java (新增或修改)

package com.hrh.class_manage_system.common.handler;

import com.hrh.class_manage_system.common.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 假设您的业务冲突异常抛出的是 RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        // ⚠️ 修复点：将 RuntimeException 包装为 Result.error()，并返回 HTTP 200 OK
        
        // 打印堆栈信息以便调试，但对前端返回友好的错误消息
        e.printStackTrace(); 
        
        // 返回 code=0 (失败)，并使用异常中的消息
        return Result.error(e.getMessage()); 
    }
    
    // 如果您使用了自定义 ServiceException，可以增加一个更精确的捕获：
    // @ExceptionHandler(ServiceException.class)
    // public Result<Void> handleServiceException(ServiceException e) {
    //     return Result.error(e.getMessage());
    // }
}