package com.sabadin.taskspringapp.aspects;

import com.sabadin.taskspringapp.aspects.annotation.AuthLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class AuthLogAspect {

    @Pointcut("@annotation(com.sabadin.taskspringapp.aspects.annotation.AuthLog)")
    public void authLogMethods() {
    }

    @Before("authLogMethods()")
    public void logMethodCall(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthLog authLog = method.getAnnotation(AuthLog.class);
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        if (authLog.showArgs()) {
            Object[] args = joinPoint.getArgs();
            String argsString = args.length > 0 ? Arrays.toString(args) : "no arguments";
            log.info("Method called: {} -> {} with arguments: {}", className, methodName, argsString);
        } else {
            log.info("Method called: {} -> {} (arguments logging disabled)", className, methodName);
        }
    }

    @AfterReturning(pointcut = "authLogMethods()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AuthLog authLog = method.getAnnotation(AuthLog.class);

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        if (authLog.showResult()) {
            String resultString = result != null ? result.toString() : "null";
            log.info("Method returned: {} -> {} with result: {}", className, methodName, resultString);
        } else {
            log.info("Method returned: {} -> {} (result logging disabled)", className, methodName);
        }
    }
}
