package com.ukma.competition.platform.images;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    Image storeImage(MultipartFile imageFile) throws IOException;
}