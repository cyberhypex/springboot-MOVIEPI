package com.movieflix.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImple implements FileService {


    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //get name of the file
        String fileName=file.getOriginalFilename();//from MultiPart file interface
        //get filePath
        String filePath=path+ File.separator+fileName; //seperator tells this two path and fileName needs to be appended

        //Create a file object
        File f=new File(path); //Create a file at that path
        if(!f.exists()){
            f.mkdir(); //mkdirectory
        }
        //copy the file or upload file to the path, // filePath is where we will be actually uploading file

        Files.copy(file.getInputStream(),Paths.get(filePath));


        return fileName; //uploaded to server
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String filePath=path+ File.separator+fileName;

        return new FileInputStream(filePath);
    }
}
