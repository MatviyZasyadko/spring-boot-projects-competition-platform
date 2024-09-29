package com.ukma.competition.platform.shared.dto.exception;

import com.ukma.competition.platform.shared.dto.exception.ExceptionDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
