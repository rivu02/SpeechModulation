package com.rudra.speechmodulationapi.controller;
import ch.qos.logback.core.net.SyslogOutputStream;
//import com.rudra.speechmodulationapi.entity.UploadFile;
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

import java.io.*;

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
        String chunkPath="";
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
                try {
                   chunkPath = givenPythonScript_whenPythonProcessInvoked_thenSuccess(fileName);
                }
                catch (Exception e ){
                    System.out.println("Unable to execute function.");
                    e.printStackTrace();
                }
//                InputStream resource=this.fileUploadService.getResource(UPLOAD_DIR,fileName);


                //return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath().path("/").path(fileName).toUriString());
                return ResponseEntity.ok(chunkPath);
            }
//
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong!!");
    }


    public String givenPythonScript_whenPythonProcessInvoked_thenSuccess(String fPath) throws Exception {
        String path = "";
        String home = System.getProperty("user.home");
        String pythonScriptPath = home+ File.separator+"Desktop"+ File.separator+ "untitled folder" + File.separator+ "asr_demo1.py";

        // Prepare the command to execute the Python script
        //String arg1="/Users/rivu/Desktop/untitled folder/test5.wav";
        String arg1=home+ File.separator+ "Downloads"+ File.separator+ "speech-modulation-api" + File.separator+fPath;

        String[] command = {"python3", pythonScriptPath, arg1};

        try {
            // Start the process and execute the Python script
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();

            // Optional: If you want to read the Python script's output, you can do so here
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                path+=line;
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();

            // Check the exit code to determine if the execution was successful
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.err.println("Python script execution failed.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return path;
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
