package com.sabadin.taskspringapp.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogExecutionTimeAspect {

    @Pointcut("@annotation(com.sabadin.taskspringapp.aspects.annotation.LogExecutionTime)")
    public void logExecutionTimeMethod(){}

    @Around("logExecutionTimeMethod()")
    public Object aroundLogExecutionTimeMethod(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object proceed = pjp.proceed();
        String methodName = pjp.getSignature().getName();
        long end = System.nanoTime();
        long executionTime = end - start;
        log.info("Method {} was executed. Execution time: {} ms./ {} ns.", methodName, (executionTime / 1_000_000), executionTime);
        return proceed;
    }
}
