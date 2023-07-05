package com.demo.demos3.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.StringUtils;
import com.demo.demos3.config.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FileComponent {

    private final AmazonS3 s3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) throws ClientException {
        try {
            Optional<File> fileOptioanl = convert(file);

            File fileToUpload = fileOptioanl.orElseThrow();

            s3Client.putObject(
                    new PutObjectRequest(bucket, file.getName(), fileToUpload)
                            .withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드 됨

            );
            return s3Client.getUrl(bucket, file.getName()).toString();
        } catch (Exception e) {
            throw new ClientException("fail to upload files", e);
        }

    }

    private Optional<File> convert(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();

        if (StringUtils.isNullOrEmpty(originalFilename)) {
            originalFilename = "temp";
        }

        File convertFile = new File(originalFilename);

        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();

    }

    public void delete(String fileName) {
        try {
            s3Client.deleteObject(bucket, fileName);
        }
        catch (Exception e) {
            throw new ClientException("fail to delete files", e);
        }
    }

    public byte[] download(String fileName) {
        try {
            S3Object s3Object = s3Client.getObject(bucket, fileName);

            String contentType = s3Object.getObjectMetadata().getContentType();

            return s3Object.getObjectContent().readAllBytes();

//        // API Response 만들어줄 때
//        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
//        ResponseEntity.ok(byteArrayResource);

        } catch (Exception e) {
            throw new ClientException("fail to download files", e);
        }

    }


}
