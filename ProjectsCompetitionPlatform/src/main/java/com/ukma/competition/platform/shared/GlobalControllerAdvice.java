package com.ukma.competition.platform.shared;

import com.ukma.competition.platform.shared.dto.exception.ExceptionDto;
import com.ukma.competition.platform.shared.dto.exception.FileEmptyExceptionDto;
import com.ukma.competition.platform.shared.exception.AuthenticationException;
import com.ukma.competition.platform.shared.exception.FileEmptyException;
import com.ukma.competition.platform.shared.exception.ImageNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.Instant;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(FileEmptyException.class)
    public ResponseEntity<ExceptionDto> fileEmptyExceptionHandler(FileEmptyException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            FileEmptyExceptionDto.fileEmptyExceptionBuilder()
                .message("please, provide not empty file!")
                .exceptionClass(exception.getClass().getName())
                .exceptionTime(Instant.now())
                .fileName(exception.getFilename())
                .build()
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionDto> ioExceptionHandler(IOException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ExceptionDto.builder()
                .message("Exception occurred during file processing: " + exception.getMessage())
                .exceptionClass(exception.getClass().getName())
                .exceptionTime(Instant.now())
                .build()
        );
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ExceptionDto> imageNotFoundExceptionHandler(ImageNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ExceptionDto.builder()
                .message("There is no file with such id! Maybe it was already deleted. FileId: " + exception.getFileId())
                .exceptionClass(exception.getClass().getName())
                .exceptionTime(Instant.now())
                .build()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public String authenticationExceptionHandler(AuthenticationException exception) {
        return "redirect:/ui/login?error=" + exception.getMessage();
    }
}
