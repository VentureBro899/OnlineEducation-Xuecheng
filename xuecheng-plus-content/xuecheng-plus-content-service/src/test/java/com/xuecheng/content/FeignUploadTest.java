package com.xuecheng.content;

import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feiginClient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/*
 * @description: Feign调用上传文件测试
 * @author: Venture
 * @date: 2023-06-03 10:44
 */
@SpringBootTest
public class FeignUploadTest {
    @Autowired
    MediaServiceClient mediaServiceClient;

    //远程调用，上传文件
    @Test
    public void test() {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(new File("D:\\nginx-1.18.0\\html\\index.html"));
        mediaServiceClient.uploadFile(multipartFile,"course//test.html");
    }

}
