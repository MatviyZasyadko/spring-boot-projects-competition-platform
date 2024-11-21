package com.ukma.competition.platform.shared.validations.image.file;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {SingleImageFileValidator.class, ListImageFileValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFile {

    String message() default "Provided file/files should have an image type!";

    boolean nullable() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
