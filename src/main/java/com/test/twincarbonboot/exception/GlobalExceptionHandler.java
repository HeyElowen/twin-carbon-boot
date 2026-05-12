package com.test.twincarbonboot.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.test.twincarbonboot.pojo.Result;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //监测层异常
    @ExceptionHandler(MonitoringException.class)
    public Result handleMonitoringException(MonitoringException e) {
        log.warn("【业务异常】监测层异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //参数校验失败
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().iterator().next().getMessage();
        log.warn("参数校验失败: {}", msg);
        return Result.error(msg);
    }
    //参数缺失
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("缺少必填参数: {}", e.getParameterName());
        return Result.error("缺少必填参数: " + e.getParameterName());
    }
    //数据库重复异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKey(DuplicateKeyException e) {
        log.warn("数据库唯一键冲突: {}", e.getMessage());
        return Result.error("数据已存在，请勿重复提交");
    }

    /** 数据库外键/唯一约束冲突（外键不存在、唯一键重复等） */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.warn("数据库约束冲突: {}", e.getMessage());
        // 简单判断消息内容，返回友好提示
        String msg = e.getMessage();
        if (msg.contains("fk_scence_category")) {
            return Result.error("所属分类不存在，无法添加场景");
        }
        if (msg.contains("uk_scence_category_year_type_quarter")) {
            return Result.error("该分类已存在，请勿重复添加");
        }
        return Result.error("数据冲突，请检查关联数据是否存在");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException e) {
        // 拿到第一个校验错误信息
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("参数校验失败: {}", msg);
        return Result.error(msg);
    }
    //意外的异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) throws NoResourceFoundException {
        // 资源未找到，交给 Spring 默认处理，返回真正的 HTTP 404
        if (e instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            throw (org.springframework.web.servlet.resource.NoResourceFoundException) e;
        }


        // ========== 诊断：打印完整异常链 ==========
        log.error("===== 进入兜底异常处理器，开始诊断 =====");
        log.error("最外层异常类型: {}", e.getClass().getName());
        log.error("最外层异常消息: {}", e.getMessage());

        Throwable current = e.getCause();
        int layer = 0;
        while (current != null) {
            layer++;
            log.error("第{}层嵌套异常 -> 类型: {}, 消息: {}",
                    layer, current.getClass().getName(), current.getMessage());
            current = current.getCause();
        }

        // ========== 解包：遍历异常链找 MonitoringException ==========
        Throwable t = e;
        while (t != null) {
            if (t instanceof MonitoringException) {
                log.warn("【自动解包】在异常链中找到 MonitoringException，转交业务处理器");
                return handleMonitoringException((MonitoringException) t);
            }
            t = t.getCause();
        }
        // 确实不是业务异常，才是真正的系统错误
        log.error("【系统异常】详细堆栈:", e);
        return Result.error("系统繁忙，请稍后重试");


    }
}