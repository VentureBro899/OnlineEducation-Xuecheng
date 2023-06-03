package com.xuecheng.content.feiginClient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/*
 * @description: 熔断后降级处理逻辑
 * @author: Venture
 * @date: 2023-06-03 15:40
 */
@Component
@Slf4j
public class MediaServiceClientFallback implements FallbackFactory<MediaServiceClient> {

    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient() {
            @Override
            public String uploadFile(MultipartFile upload, String objectName) {
                //降级方法
                log.debug("调用媒资管理服务上传文件时发生熔断，异常信息:{}",throwable.toString(),throwable);
                return null;
            }
        };
    }
}
