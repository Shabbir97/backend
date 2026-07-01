package com.propertyfinder.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String FOLDER_PATH = "uploads/";

    public String uploadImage(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        File directory = new File(FOLDER_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File destination = new File(FOLDER_PATH + fileName);
        file.transferTo(destination);

        return "uploads/" + fileName;
    }
}