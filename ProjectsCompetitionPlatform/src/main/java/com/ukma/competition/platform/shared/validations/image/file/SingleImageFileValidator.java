package com.ukma.competition.platform.shared.validations.image.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SingleImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    boolean nullable;

    @Override
    public void initialize(ImageFile constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return nullable;
        }

        return multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image/");
    }
}
