package com.demo.demos3.service;


import com.demo.demos3.config.FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final FileComponent fileComponent;

    public String upload(MultipartFile file) {
        String upload = fileComponent.upload(file);
        // db에 upload url 저장
        return upload;
    }

}
