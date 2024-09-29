package com.ukma.competition.platform.shared.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ImageNotFoundException extends RuntimeException {

    String fileId;

    public ImageNotFoundException(String message, String fileId) {
        super(message);
        this.fileId = fileId;
    }
}
