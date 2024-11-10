package com.ukma.competition.platform.aspects;

import com.ukma.competition.platform.performance.trackers.PerformanceTrackerRecordEntity;
import com.ukma.competition.platform.performance.trackers.PerformanceTrackerRecordRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PerformanceTrackerAspect {

    PerformanceTrackerRecordRepository performanceTrackerRecordRepository;

    @Pointcut("within(@com.ukma.competition.platform.shared.annotations.PerformanceTracker *)")
    public void beanAnnotatedWithPerformanceTracker() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
    }

    @Pointcut("publicMethod() && beanAnnotatedWithPerformanceTracker()")
    public void publicMethodInsideAClassMarkedWithAtMonitor() {}

    @Around("publicMethodInsideAClassMarkedWithAtMonitor()")
    public Object methodPerformancePointCut(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Start a method '{}' performance process", pjp.getSignature().getName());
        Throwable methodThrowable = null;
        long timeBeforeExecution = System.currentTimeMillis();
        Object methodResult = null;
        try {
            methodResult = pjp.proceed();
        } catch (Throwable exception) {
            methodThrowable = exception;
            log.info("Method '{}' threw an error {}", pjp.getSignature().getName(), methodThrowable.getClass().getName());
        }
        long timeAfterExecution = System.currentTimeMillis();
        Double executionTime = (timeAfterExecution - timeBeforeExecution) / 1000.;
        log.info("Method '{}' from class {} time execution: {} s", pjp.getSignature().getName(), pjp.getSignature().getDeclaringType().getName(), executionTime);
        performanceTrackerRecordRepository.save(
            PerformanceTrackerRecordEntity.builder()
                .className(pjp.getSignature().getDeclaringType().getName())
                .methodName(pjp.getSignature().getName())
                .processTime(executionTime)
                .isSuccessful(methodThrowable == null)
                .build()
        );
        if (methodThrowable != null) {
            throw methodThrowable;
        }
        return methodResult;
    }
}
