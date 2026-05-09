package com.studygroup.studygroup.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MainServiceLoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(MainServiceLoggingAspect.class);

    @Before("execution(* com.studygroup.studygroup.Service..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {

        logger.info(
                "START: {}",
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.studygroup.studygroup.Service..*.*(..))"
    )
    public void logSuccess(JoinPoint joinPoint) {

        logger.info(
                "SUCCESS: {}",
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.studygroup.studygroup.Service..*.*(..))",
            throwing = "ex"
    )
    public void logError(JoinPoint joinPoint, Exception ex) {

        logger.error(
                "FAILED: {} -> {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage()
        );
    }
}