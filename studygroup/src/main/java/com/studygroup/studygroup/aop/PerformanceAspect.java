package com.studygroup.studygroup.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(* com.studygroup.studygroup.Service..*.*(..))")
    public Object measureExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        logger.info(
                "{} executed in {} ms",
                joinPoint.getSignature().toShortString(),
                (end - start)
        );

        return result;
    }
}