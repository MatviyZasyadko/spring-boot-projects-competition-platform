package com.ukma.competition.platform.aspects;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationLimiterAspect {

    final private Integer METHOD_CALL_LIMIT_VALUE;
    ConcurrentMap<String, Integer> methodsCallsAmountMap;

    public AuthenticationLimiterAspect() {
        this.methodsCallsAmountMap = new ConcurrentHashMap<>();
        this.METHOD_CALL_LIMIT_VALUE = 100;
    }

    @Pointcut("within(com.ukma.competition.platform.auth.AuthenticationService+)")
    public void authenticationServicePointCut() {
    }

    @Before("authenticationServicePointCut()")
    public void limitAuthenticationMethodsCalls(JoinPoint joinPoint) {
        Signature methodSignature = joinPoint.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getClass().getName();
        Integer methodCallsAmount = methodsCallsAmountMap.getOrDefault(methodName, 0);

        log.info("Before calling a {} method from {} class", methodName, className);

        if (methodCallsAmount >= METHOD_CALL_LIMIT_VALUE) {
            String errorMessage = "Exceeded max limit of calls for method " + methodName + " from class " + className;
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        methodsCallsAmountMap.put(methodName, methodsCallsAmountMap.getOrDefault(methodName, 0) + 1);
    }


    @Scheduled(cron = "0 * * * * *")
    public void resetMethodsCallsMap() {
        this.methodsCallsAmountMap = new ConcurrentHashMap<>();
    }
}
