package com.ukma.competition.platform.shared.validations.image.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListImageFileValidator implements ConstraintValidator<ImageFile, List<MultipartFile>> {

    boolean nullable;

    @Override
    public void initialize(ImageFile constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(List<MultipartFile> fileList, ConstraintValidatorContext constraintValidatorContext) {
        if (fileList == null || fileList.isEmpty()) {
            return nullable;
        }

        return fileList.stream().allMatch(
            file -> file.getContentType() != null && file.getContentType().startsWith("image/")
        );
    }
}
