package com.ukma.competition.platform.shared.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class WithinTwoYearsValidator implements ConstraintValidator<WithinTwoYears, Instant> {

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Instant now = Instant.now();
        Instant twoYearsFromNow = now.plus(2, ChronoUnit.YEARS);

        return value.isBefore(twoYearsFromNow);
    }
}
