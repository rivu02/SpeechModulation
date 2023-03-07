package com.rudra.speechmodulationapi.controller;
import com.rudra.speechmodulationapi.entity.UploadFile;
import com.rudra.speechmodulationapi.handeler.FileUploadHandler;
import com.rudra.speechmodulationapi.service.FileUploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileUploadController {

    @Autowired
    FileUploadHandler fileUploadHandler;

    @Value("${project.image}")
    private  String UPLOAD_DIR;

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(path = "/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile f){
        try{
            if(f.isEmpty()){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("file contain is empty!!");
            }
            if(!f.getContentType().equalsIgnoreCase("audio/wave")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("file type must be wav!!");
            }

            //upload file
            String fileName=fileUploadHandler.uploadHandler(f);
            if(!fileName.isEmpty()){
//                InputStream resource=this.fileUploadService.getResource(UPLOAD_DIR,fileName);
                return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath().path("/").path(fileName).toUriString());
            }
//
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong!!");
    }

    @GetMapping(value = "/{audioName}",produces = MediaType.ALL_VALUE)
    public void downloadFile(
            @PathVariable("audioName") String audioName,
            HttpServletResponse response
    )throws IOException {
        InputStream resource=this.fileUploadService.getResource(UPLOAD_DIR,audioName);
        response.setContentType(MediaType.ALL_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
