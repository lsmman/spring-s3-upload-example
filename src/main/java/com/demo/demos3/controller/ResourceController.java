package com.demo.demos3.controller;

import com.demo.demos3.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam(value = "file") MultipartFile image) {
        return resourceService.upload(image);
    }

}
