package com.ukma.competition.platform.shared.dto.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
public class FileEmptyExceptionDto extends ExceptionDto {

    String fileName;

    @Builder(builderMethodName = "fileEmptyExceptionBuilder")
    public FileEmptyExceptionDto(
        String message,
        String exceptionClass,
        Instant exceptionTime,
        String fileName
    ) {
        super(message, exceptionClass, exceptionTime);
        this.fileName = fileName;
    }
}
