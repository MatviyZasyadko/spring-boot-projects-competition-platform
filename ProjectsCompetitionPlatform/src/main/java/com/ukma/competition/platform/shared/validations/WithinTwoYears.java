package com.ukma.competition.platform.shared.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = WithinTwoYearsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WithinTwoYears {
    String message() default "Date must be within 2 years from now";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
