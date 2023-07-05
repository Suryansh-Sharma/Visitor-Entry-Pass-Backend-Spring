package com.suryansh.visitorentry.service;

import com.suryansh.visitorentry.exception.SpringVisitorException;
import com.suryansh.visitorentry.service.interfaces.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This Service class is used for performing file related operation.
 *
 * @author suryansh
 */
@Service
public class FileServiceImpl implements FileService {
    @Value("${folder.images}")
    private String FOLDER_PATH;
    private final static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String addNewImage(MultipartFile file, String imageName) {
        if (file.isEmpty()){
            throw new SpringVisitorException("Image is empty", ErrorType.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (imageName.length()==0){
            throw new SpringVisitorException("Image name is empty", ErrorType.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        String newFilePath = FOLDER_PATH + "/" + imageName;
        File newFile = new File(newFilePath);

        try {
            file.transferTo(newFile);
            logger.info("New Image {} is added successfully", imageName);
            return imageName;
        } catch (Exception e) {
            logger.error("Unable to add new image file: " + e);
            throw new SpringVisitorException("Unable to update file", ErrorType.INTERNAL_ERROR, HttpStatus.BAD_REQUEST);
        }
    }

    public byte[] getImage(String name) throws IOException {
        String fullPath = FOLDER_PATH + "/" + name;
        File file = new File(fullPath);
        if (!file.exists()) throw new SpringVisitorException("Unable to find image of name " + name,ErrorType.NOT_FOUND,HttpStatus.NOT_FOUND);
        return Files.readAllBytes(file.toPath());
    }
}
