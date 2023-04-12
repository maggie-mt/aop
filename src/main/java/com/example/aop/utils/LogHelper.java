package com.example.aop.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogHelper {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    // 定义切点(pointcut)
    // 测试发现对同一方法加Before/Around/After切点, 好像没有明确执行顺序

    // 1. Before所有Controller的所有方法
    @Pointcut(value = "execution(* com.example.aop.controller.*.*(..))")
    public void aopBeforeLog() {
    }

    // 2. Around BController的所有方法
    @Pointcut(value = "execution(* com.example.aop.controller.BController.*(..))")
    public void aopAroundLog() {
    }

    // 3. After所有Controller的所有方法
    @Pointcut(value = "execution(* com.example.aop.controller.*.*(..))")
    public void aopAfterLog() {
    }

    @Before("aopBeforeLog()")
    public void beforeLogger(JoinPoint jp) throws Throwable {
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();
        // 使用数组来获取参数
        Object[] array = jp.getArgs();
        ObjectMapper mapper = new ObjectMapper();
        // 打印日志
        logger.info("Before: {}.{}, 开始执行", className, methodName);
    }

    // 使用环绕通知, 获取ProceedingJoinPoint信息, 并记录用时
    @Around("aopAroundLog()")
    public Object aroundLogger(ProceedingJoinPoint pjp) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 使用ServletRequestAttributes请求上下文获取方法
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        // 使用数组和Mapper
        Object[] array = pjp.getArgs();
        ObjectMapper mapper = new ObjectMapper();
        // 执行函数前打印日志
        logger.info("环绕Before: {}.{}, 传递的参数为: {}", className, methodName, mapper.writeValueAsString(array));
        logger.info("URL: {}", request.getRequestURL().toString());
        logger.info("IP地址: {}", request.getRemoteAddr());
        // 调用目标函数执行
        Object obj = pjp.proceed();
        // 执行函数后打印日志
        logger.info("环绕After: {}.{}, 返回值为: {}", className, methodName, mapper.writeValueAsString(obj));
        logger.info("耗时: {}ms", System.currentTimeMillis() - startTime.get());
        return obj;
    }

    @After("aopAfterLog()")
    public void afterLogger(JoinPoint jp) throws Throwable {
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();
        // 使用数组来获取参数
        Object[] array = jp.getArgs();
        ObjectMapper mapper = new ObjectMapper();
        // 打印日志
        logger.info("After: {}.{}, 执行结束", className, methodName);
    }
}