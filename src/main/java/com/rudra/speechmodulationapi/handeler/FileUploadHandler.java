package com.rudra.speechmodulationapi.handeler;

import com.rudra.speechmodulationapi.entity.UploadFile;
import com.rudra.speechmodulationapi.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Component
public class FileUploadHandler {

    @Autowired
    FileUploadService fileUploadService;
//    private final String UPLOAD_DIR="C:\\Users\\monis\\Desktop\\Java-Spring\\speech-modulation-api\\speech-modulation-api\\src\\main\\resources\\static\\file";
    @Value("${project.image}")
    private String UPLOAD_DIR; //=new ClassPathResource("static/file").getFile().getAbsolutePath();

    public FileUploadHandler() throws IOException {
    }

//    public String uploadHandler(MultipartFile f){
//        String fileName=f.getOriginalFilename();
//        String uniqueID = UUID.randomUUID().toString();
//
//        String extension = com.google.common.io.Files.getFileExtension(fileName).toLowerCase();
//
////        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
////        Instant timestamp = Instant.now();
//
//        String new_file_name = "AUD-" + uniqueID +"." + extension;
//        String upload_path=UPLOAD_DIR+File.separator+new_file_name;
//
//        System.out.println(new_file_name);
//        try{
//            Files.copy(f.getInputStream(), Path.of(upload_path), StandardCopyOption.REPLACE_EXISTING);
//
//            UploadFile uploadFile=new UploadFile();
//            uploadFile.setFileName(new_file_name);
//            uploadFile.setFilePath(upload_path);
//            uploadFile.setCreatedAt(new Date());
//
//            fileUploadService.uploadFile(uploadFile);
//            return new_file_name;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String uploadHandler(MultipartFile f){
        File file=new File(UPLOAD_DIR);

        if(!file.exists()){
            file.mkdir();
        }
        String fileName=f.getOriginalFilename();

        String uniqueID = UUID.randomUUID().toString();

        String extension = com.google.common.io.Files.getFileExtension(fileName).toLowerCase();

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
//        Instant timestamp = Instant.now();

        String new_file_name = "AUD-" + uniqueID +"." + extension;
        String upload_path=UPLOAD_DIR+new_file_name;

        System.out.println(new_file_name);
        try{
            Files.copy(f.getInputStream(), Path.of(upload_path), StandardCopyOption.REPLACE_EXISTING);

            UploadFile uploadFile=new UploadFile();
            uploadFile.setFileName(new_file_name);
            uploadFile.setFilePath(upload_path);
            uploadFile.setCreatedAt(new Date());

            fileUploadService.uploadFile(uploadFile);
            return new_file_name;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
