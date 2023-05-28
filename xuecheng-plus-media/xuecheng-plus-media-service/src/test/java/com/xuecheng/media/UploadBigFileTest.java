package com.xuecheng.media;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/*
 * @description: 大文件断点续传
 * @author: Venture
 * @date: 2023-05-14 17:17
 */
@SpringBootTest
public class UploadBigFileTest {
    @Test
    void chunk() throws Exception {
        String folder = "E:\\网课学习笔记\\mksz626-Springboot+netty开发即时通讯系统\\";
        File bigFile = new File(folder +
                "{3}--第3章基础数据开发-用户&amp;关系&amp;群组\\3-2 业务系统的基石 - 用户模块业务分析&数据库设计.mp4");
        RandomAccessFile randomAccessFile = new RandomAccessFile(bigFile,"r");
        // 分块大小
        int chunksize = (int)1024 * 1024;
        // 缓冲区
        byte[] buf = new byte[1024];
        // 总块数
        int total = (int)Math.ceil(bigFile.length() * 1.0 / chunksize);
        String name = bigFile.getName().substring(0,bigFile.getName().indexOf("."));
        for (int i = 0; i < total; i++) {
            File file = new File("E:\\temp\\" + i);
            if(file.exists())
                file.delete();
            RandomAccessFile out_chunk = new RandomAccessFile(file, "rw");
            while (randomAccessFile.read(buf) != -1) {
                out_chunk.write(buf);
                if (file.length() >= chunksize) // 达到分块大小上限则进行下一分块
                    break;
            }
            out_chunk.close();
        }
        randomAccessFile.close();

    }

    @Test
    void merge() throws IOException {
        // 大文件目录
        String folder = "C:\\Users\\Venture\\Desktop\\";
        // 分块目录
        String chunkFolder = "E:\\temp";
        File file = new File(chunkFolder);
        // 获取所有分块文件
        File[] chunks = file.listFiles();
        // 对所有分块文件进行排序
        Arrays.sort(chunks,(o1, o2) -> {
            return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
        });
        File merge = new File(folder + "1.mp4");
        if (merge.exists())
            merge.delete();
        // 创建大文件
        merge.createNewFile();
        RandomAccessFile mergeFile = new RandomAccessFile(merge,"rw");
        mergeFile.seek(0);
        byte[] buf = new byte[1024];
        for (File chunkFile : chunks) {
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
            int len = -1;
            while ((len = raf_read.read(buf)) != -1) {
                mergeFile.write(buf, 0, len);
            }
            raf_read.close();
        }
        System.out.println("文件成功写出到" + folder + ".mp4");
        mergeFile.close();
    }
}
