package com.adminmicroservice.adminmicroservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminLoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(AdminLoggingAspect.class);

    @Before("execution(* com.adminmicroservice.adminmicroservice.Service..*.*(..))")
    public void before(JoinPoint jp) {

        logger.info(
                "START: {}",
                jp.getSignature().toShortString()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.adminmicroservice.adminmicroservice.Service..*.*(..))"
    )
    public void success(JoinPoint jp) {

        logger.info(
                "SUCCESS: {}",
                jp.getSignature().toShortString()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.adminmicroservice.adminmicroservice.Service..*.*(..))",
            throwing = "ex"
    )
    public void error(JoinPoint jp, Exception ex) {

        logger.error(
                "FAILED: {} -> {}",
                jp.getSignature().toShortString(),
                ex.getMessage()
        );
    }
}