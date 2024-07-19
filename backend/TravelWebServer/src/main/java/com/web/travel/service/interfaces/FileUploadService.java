package com.web.travel.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService{
    String uploadFile(MultipartFile multipartFile) throws IOException;
    List<String> uploadMultiFile(MultipartFile[] files) throws IOException;
 }
