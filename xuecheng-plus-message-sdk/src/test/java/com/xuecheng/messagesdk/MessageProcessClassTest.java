package com.xuecheng.messagesdk;

/*
 * @description: 测试类
 * @author: Venture
 * @date: 2023-06-02 20:52
 */

import com.xuecheng.messagesdk.service.MqMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class MessageProcessClassTest {
    @Autowired
    MessageProcessClass messageProcessClass;

    @Autowired
    MqMessageService mqMessageService;

    @Test
    void testMessageProcess() throws InterruptedException {
        System.out.println("开始执行-----》" + LocalDateTime.now());
        messageProcessClass.process(0, 1, "test", 5, 30);
        System.out.println("结束执行-----》" + LocalDateTime.now());
        Thread.sleep(9000000);
    }

    @Test
    void testAddMg(){
        for (int i = 0; i < 20; i++) {
            mqMessageService.addMessage("test","2",null,null);
        }

    }
}
