package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * 媒资文件管理
 * @param null
 * @return
 * @author venture
 * @creed: Nothing Ventured,nothing gained
 * @date 2023-05-14 12:28
 */

public interface MediaFileService {

    /**
     * @description 媒资文件查询方法
     * @param pageParams 分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     */
    public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
     * 上传文件
     * @param companyId 机构id
     * @param uploadFileParamsDto 上传文件信息
     * @param localFilePath 文件磁盘路径
     * @return 文件信息
     */
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath);


    /**
     * @description 检查文件是否存在
     * @param fileMd5 大文件的md5
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     */
    public RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * @description 检查分块是否存在
     * @param fileMd5  大文件的md5
     * @param chunkIndex  分块序号
     * @return com.xuecheng.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     */
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);


    /**
     * @description 上传分块
     * @param fileMd5  大文件md5
     * @param chunk  分块序号
     * @param bytes  文件字节
     * @return com.xuecheng.base.model.RestResponse
     * @date 2022/9/13 15:50
     */
    public RestResponse uploadChunk(String fileMd5,int chunk,byte[] bytes);

    /**
     * @description 合并分块
     * @param companyId  机构id
     * @param fileMd5  文件md5
     * @param chunkTotal 分块总和
     * @param uploadFileParamsDto 文件信息
     * @return com.xuecheng.base.model.RestResponse
     */
    public RestResponse mergechunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);


    /**
     * 从minio下载文件
     * @param bucket 桶
     * @param objectName 对象名称
     * @return 下载后的文件
     */
    public File downloadFileFromMinIO(String bucket, String objectName);

    /**
     * 上传文件到minio分布式文件系统
     * @param bucketName 容器名
     * @param localFilePath 本地文件路径
     * @param objectName 上传到文件系统中的对象名
     * @param mineType mine类型
     * @return boolean
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-13 23:45
     */

    @Autowired
    public boolean uploadFileToMinio(String bucketName,String localFilePath,String objectName,String mineType);

}
