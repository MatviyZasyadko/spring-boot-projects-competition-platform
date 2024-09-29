package com.ukma.competition.platform.shared.dto.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidationFailDto extends ExceptionDto {

    List<String> fieldErrors = new ArrayList<>();

    @Builder(builderMethodName = "validationFailBuilder")
    public ValidationFailDto(String message, String exceptionClass, Instant exceptionTime, List<String> fieldErrors) {
        super(message, exceptionClass, exceptionTime);
        this.fieldErrors = fieldErrors;
    }
}
