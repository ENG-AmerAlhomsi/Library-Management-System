package com.project.library_management_system.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for BookService methods
    @Pointcut("execution(public * com.project.library_management_system.service.BookService.*(..))")
    public void bookServiceMethods() {}

    // Pointcut for PatronService methods
    @Pointcut("execution(public * com.project.library_management_system.service.PatronService.*(..))")
    public void patronServiceMethods() {}

    // Pointcut for BorrowingService methods
    @Pointcut("execution(public * com.project.library_management_system.service.BorrowingService.*(..))")
    public void borrowingServiceMethods() {}

    // Log method entry and arguments
    @Before("bookServiceMethods() || patronServiceMethods() || borrowingServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        LOGGER.info("Method [{}] called with arguments: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs())
        );
    }

    // Log method exit and return value
    @AfterReturning(pointcut = "bookServiceMethods() || patronServiceMethods() || borrowingServiceMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        LOGGER.info("Method [{}] returned: {}",
                joinPoint.getSignature().toShortString(),
                result != null ? result.toString() : "void"
        );
    }

    // Log exceptions
    @AfterThrowing(pointcut = "bookServiceMethods() || patronServiceMethods() || borrowingServiceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        LOGGER.error("Exception in method [{}]: {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage()
        );
    }

    // Log performance metrics (execution time)
    @Around("bookServiceMethods() || patronServiceMethods() || borrowingServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        LOGGER.info("Method [{}] executed in {} ms",
                joinPoint.getSignature().toShortString(),
                executionTime
        );
        return result;
    }
}
