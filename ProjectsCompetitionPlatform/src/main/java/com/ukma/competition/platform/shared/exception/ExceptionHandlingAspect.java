package com.ukma.competition.platform.shared.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class ExceptionHandlingAspect {
    private static final Logger logger = LogManager.getLogger(ExceptionHandlingAspect.class);

    @Value("${log.file.path}")
    private String LOG_FILE_PATH;

    @Pointcut("@annotation(com.ukma.competition.platform.shared.exception.HandleExceptions)")
    public void handleExceptionPointcut() {}

    @AfterThrowing(pointcut = "handleExceptionPointcut()", throwing = "ex")
    public void logAfterThrowingException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Exception occurred in method " + methodName + " with @HandleExceptions annotation: ", ex);
        fileLogging(ex);
    }

    private void fileLogging(Exception ex) {
        String exceptionMessage = String.format(
                "[%s] Exception: %s%nStacktrace:%n%s%n%n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ex.getMessage(),
                getStackTraceAsString(ex)
        );

        Path logFilePath = Paths.get(LOG_FILE_PATH);

        try {
            if (!Files.exists(logFilePath.getParent())) {
                Files.createDirectories(logFilePath.getParent());
            }
        } catch (IOException ioEx) {
            logger.error("Failed to create log directory", ioEx);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(logFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(exceptionMessage);
            logger.info("Exception written to file: " + logFilePath.toAbsolutePath());
        } catch (IOException ioEx) {
            logger.error("Failed to write exception to file", ioEx);
        }
    }

    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
