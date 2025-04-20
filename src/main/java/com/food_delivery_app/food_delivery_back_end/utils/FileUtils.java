package com.food_delivery_app.food_delivery_back_end.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUtils {
    public static final String UPLOAD_DIR = "uploads";

    public static String storeFile(MultipartFile file) throws Exception {
        // Tạo thư mục upload nếu chưa tồn tại
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // Sinh tên file duy nhất
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        // Đường dẫn lưu file
        Path targetLocation = uploadPath.resolve(newFileName);

        // Sao chép file
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn tương đối
        return "/" + newFileName;
    }

    public static boolean checkFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new RuntimeException("Invalid file type");
        }
        return true;
    }
}
