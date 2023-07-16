package ru.practicum.shareit.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Aspect
@Repository
@Slf4j
public class LoggingAspect {
    @Around("@within(ru.practicum.shareit.annotation.ToLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        log.trace("Method " + methodName +
                " with parameters " + Arrays.asList(arguments) +
                " will execute");

        Object returnedByMethod = joinPoint.proceed();

        log.trace("Method " + methodName +
                " executed and returned " + returnedByMethod);

        return returnedByMethod;
    }
}
