package com.movieflix.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(String path,MultipartFile file) throws IOException;

    InputStream getResource(String path,String fileName) throws FileNotFoundException;  //Get the data
    //fetch data from databse and then generate url
}
