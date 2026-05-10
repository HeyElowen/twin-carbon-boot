package com.test.twincarbonboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.test.twincarbonboot.controller.*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("traceId", traceId);

        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();

        long start = System.currentTimeMillis();
        log.info("[{}] [{}.{}] 请求: {}", traceId, className, methodName, Arrays.toString(args));

        Object result;
        try {
            result = point.proceed();
            long cost = System.currentTimeMillis() - start;

            String preview = result != null ? result.toString() : "null";
            preview = preview.length() > 200 ? preview.substring(0, 200) + "..." : preview;
            log.info("[{}] [{}.{}] 成功, 耗时:{}ms, 响应: {}", traceId, className, methodName, cost, preview);

        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            log.error("[{}] [{}.{}] 异常, 耗时:{}ms, {}", traceId, className, methodName, cost, e.getMessage());
            throw e;
        } finally {
            MDC.clear();
        }

        return result;
    }
}
