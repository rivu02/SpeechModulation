package com.rudra.speechmodulationapi.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.rudra.speechmodulationapi.entity.UploadFile;
import com.rudra.speechmodulationapi.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
public class FileUploadService {

    @Autowired
    FileUploadRepository fileUploadRepository;



    public void uploadFile(UploadFile uf){
        System.out.println(uf);
        fileUploadRepository.save(uf);
    }

    public InputStream getResource(String path,String fileName) throws FileNotFoundException {
        String fullPath=path+ File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }
}
