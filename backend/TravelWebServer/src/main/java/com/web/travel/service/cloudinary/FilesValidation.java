package com.web.travel.service.cloudinary;

import com.web.travel.model.enums.EStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class FilesValidation {
    public EStatus validate(MultipartFile[] files){
        if((files.length == 1 && Objects.requireNonNull(files[0].getOriginalFilename()).isEmpty())){
            return EStatus.STATUS_EMPTY_FILE;
        }
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            if(name != null && !name.isEmpty()) {
                String[] splited = name.split("\\.");
                String ext = splited[splited.length - 1];

                if (!ext.equals("png") && !ext.equals("jpg") &&
                        !ext.equals("jpeg") && !ext.equals("gif")) {
                    return EStatus.STATUS_WRONG_EXT;
                }
            }
        }
        return EStatus.STATUS_OK;
    }

    public boolean isCorrectFormat(MultipartFile file){
        String name = file.getOriginalFilename();
        if(name != null && !name.isEmpty()) {
            String[] splited = name.split("\\.");
            String ext = splited[splited.length - 1];

            return ext.equals("png") || ext.equals("jpg") ||
                    ext.equals("jpeg") || ext.equals("gif");
        }

        return true;
    }
}
