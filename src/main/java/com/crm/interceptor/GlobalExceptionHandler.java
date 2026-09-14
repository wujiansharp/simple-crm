package com.crm.interceptor;
import com.crm.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 自定义/运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeException(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    // 全局兜底异常
    @ExceptionHandler(Exception.class)
    public Result<?> exception(Exception e) {
        e.printStackTrace();
        return Result.fail("系统繁忙，请稍后重试");
    }
}