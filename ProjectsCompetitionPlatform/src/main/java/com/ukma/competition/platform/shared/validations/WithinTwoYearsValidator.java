package com.ukma.competition.platform.shared.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class WithinTwoYearsValidator implements ConstraintValidator<WithinTwoYears, Instant> {

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // Let @NotNull handle the null check
        }

        // Get the current date
        Instant now = Instant.now();

        // Calculate two years from now
        Instant twoYearsFromNow = now.plus(2, ChronoUnit.YEARS);

        // Check if the value is within the next two years
        return value.isBefore(twoYearsFromNow);
    }
}
