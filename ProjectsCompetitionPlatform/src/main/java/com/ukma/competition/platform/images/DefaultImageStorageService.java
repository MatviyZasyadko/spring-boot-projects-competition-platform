package com.ukma.competition.platform.images;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
// Реалізація, для майбутньої імплементації різних способів збереження зображень, наприклад у хмарі або локально і тд
@Service
@ConditionalOnMissingBean(ImageStorageService.class) // Завантажити, якщо немає іншого ImageStorageService
public class DefaultImageStorageService implements ImageStorageService {

    private final String storageDirectory = "uploaded_images"; // Директорія для зберігання зображень


    public DefaultImageStorageService() {
        File directory = new File(storageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public Image storeImage(MultipartFile imageFile) throws IOException {
        String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(storageDirectory, imageName);

        Files.copy(imageFile.getInputStream(), imagePath);

        return new Image(imageName, imagePath.toString());
    }
}