package com.xuecheng.media.config;

/*
 * @description: minio配置类
 * @author: Venture
 * @date: 2023-05-13 21:55
 */

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucket.files}")
    private String mediafiles;
    @Value("${minio.bucket.videofiles}")
    private String video;

    @Bean
    public MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, secretKey)
                        .build();
        boolean flag1 = minioClient.bucketExists(BucketExistsArgs.builder().bucket(mediafiles).build());
        boolean flag2 = minioClient.bucketExists(BucketExistsArgs.builder().bucket(video).build());
        if(!flag1 || !flag2){
            throw new RuntimeException("容器" + (flag1 ? video : (flag2 ? mediafiles : video + "和" + mediafiles)) + "不存在");
        }
        return minioClient;
    }
}
