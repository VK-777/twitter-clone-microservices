package com.group1.tweetservice.utility;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Log all methods in service and controller layers
    @Pointcut("within(com.group1.tweetservice.service..*) || within(com.group1.tweetservice.api..*)")
    public void applicationLayer() {}

    // Log entry, exit and execution time
    @Around("applicationLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className  = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        log.info("→ ENTER {}.{}() | args: {}",
                className, methodName, Arrays.toString(joinPoint.getArgs()));

        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            log.error("✗ EXCEPTION in {}.{}() | message: {}",
                    className, methodName, ex.getMessage());
            throw ex;
        }
        long elapsed = System.currentTimeMillis() - start;

        log.info("← EXIT  {}.{}() | took {}ms", className, methodName, elapsed);
        return result;
    }

    // Log exceptions thrown from any layer
    @AfterThrowing(pointcut = "applicationLayer()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("✗ EXCEPTION in {}.{}() → {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
