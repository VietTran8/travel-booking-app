package com.web.travel.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.web.travel.model.enums.EStatus;
import com.web.travel.service.interfaces.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final FilesValidation filesValidation;
    private final Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty() || Objects.requireNonNull(multipartFile.getOriginalFilename()).isEmpty()
        || !filesValidation.isCorrectFormat(multipartFile)){
            return null;
        }
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    @Override
    public List<String> uploadMultiFile(MultipartFile[] files) throws IOException {
        EStatus eStatus = filesValidation.validate(files);
        if (Objects.requireNonNull(eStatus) == EStatus.STATUS_EMPTY_FILE) {
            throw new IOException("Lack of files");
        }
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(uploadFile(file));
        }
        return fileNames;
    }
}
