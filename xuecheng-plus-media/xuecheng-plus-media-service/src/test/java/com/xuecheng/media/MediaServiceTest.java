package com.xuecheng.media;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * @description: T测试
 * @author: Venture
 * @date: 2023-05-13 21:00
 */
@SpringBootTest
public class MediaServiceTest {
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://996so.icu:9000")
                    .credentials("XVRCPO2Y1S5E9LZFWNOE", "zsIoRs5Iyh+slFhACVctSwh07zY+WnaAE1bEsPz+")
                    .build();

    //上传文件
    @Test
    public  void upload() {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("xcbucket")
                    .object("md/GTA介绍补档.md")//添加子目录
                    .filename("C:\\Users\\Venture\\Desktop\\GTA介绍补档.md")
                    .contentType("text/markdown")//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }

}
