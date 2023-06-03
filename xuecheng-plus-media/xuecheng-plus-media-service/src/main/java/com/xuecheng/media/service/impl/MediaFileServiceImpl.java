package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileService;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    MediaFilesMapper mediaFilesMapper;

    @Autowired
    MinioClient minioClient;

    @Autowired
    MediaFileServiceImpl currentProxy;

    @Autowired
    MediaProcessMapper mediaProcessMapper;

    //普通文件桶
    @Value("${minio.bucket.files}")
    private String bucket_Files;

    @Value("${minio.bucket.videofiles}")
    private String bucket_videoFiles;





    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();

        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }

    // 上传小文件
    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath,String objectName) {
        File file = new File(localFilePath);
        if (!file.exists()) {
            XueChengPlusException.cast("文件不存在");
        }
        String fileMd5 = getFileMd5(file);
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null){ // 文件已存在数据库中，则直接返回文件信息
            UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
            BeanUtils.copyProperties(mediaFiles,uploadFileResultDto);
            return uploadFileResultDto;
        }
        String filename = uploadFileParamsDto.getFilename();
        // 文件扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        // 默认文件夹
        String defaultFolderPath = getDefaultFolderPath();
        // 对象名为文件夹+md5值+扩展名的形式
        if (StringUtils.isBlank(objectName))
            objectName =  defaultFolderPath + "/" + fileMd5 + extension;
        // 文件mine类型
        String mineType = getMimeType(extension);
        // 将文件上传到分布式文件系统
        uploadFileToMinio(bucket_Files,localFilePath, objectName,mineType);
        // 以下是将文件信息添加到数据库的处理
        mediaFiles = currentProxy.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_Files, objectName);
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles,uploadFileResultDto);
        return uploadFileResultDto;
    }

    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        //查询文件信息
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            //桶
            String bucket = mediaFiles.getBucket();
            //存储目录
            String filePath = mediaFiles.getFilePath();
            //文件流
            InputStream stream = null;
            try {
                stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(filePath)
                                .build());

                if (stream != null) {
                    //文件已存在
                    return RestResponse.success(true);
                }
            } catch (Exception e) {

            }
        }
        //文件不存在
        return RestResponse.success(false);

    }

    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {

        //得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        //文件流
        try(InputStream fileInputStream = minioClient.getObject(
                GetObjectArgs.builder()
                            .bucket(bucket_videoFiles)
                .object(chunkFilePath)
                .build())) {
            if (fileInputStream != null) {
                //分块已存在
                return RestResponse.success(true);
            }
        } catch (Exception e) {

        }
        //分块未存在
        return RestResponse.success(false);
    }

    @Override
    public RestResponse uploadChunk(String fileMd5, int chunk, byte[] bytes) {


        //得到分块文件的目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //得到分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunk;

        try {
            //将文件存储至minIO
            uploadFileToMinio(bytes, bucket_videoFiles,chunkFilePath);
            return RestResponse.success(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug("上传分块文件:{},失败:{}",chunkFilePath,ex.getMessage());
        }
        return RestResponse.validfail(false,"上传分块失败");
    }


    @Override
    public RestResponse mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        //=====获取分块文件路径=====
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //将分块文件路径组成 List<ComposeSource>
        List<ComposeSource> sourceObjectList = Stream.iterate(0, i -> ++i)
                .limit(chunkTotal)
                .map(i -> ComposeSource.builder()
                        .bucket(bucket_videoFiles)
                        .object(chunkFileFolderPath.concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());
        //=====合并=====
        //文件名称
        String fileName = uploadFileParamsDto.getFilename();
        //文件扩展名
        String extName = fileName.substring(fileName.lastIndexOf("."));
        //合并文件路径
        String mergeFilePath = getFilePathByMd5(fileMd5, extName);
        try {
            //合并文件
            ObjectWriteResponse response = minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucket_videoFiles)
                            .object(mergeFilePath)
                            .sources(sourceObjectList)
                            .build());
            log.debug("合并文件成功:{}",mergeFilePath);
        } catch (Exception e) {
            log.debug("合并文件失败,fileMd5:{},异常:{}",fileMd5,e.getMessage(),e);
            return RestResponse.validfail(false, "合并文件失败。");
        }

        // ====验证md5====
        File minioFile = downloadFileFromMinIO(bucket_videoFiles,mergeFilePath);
        if(minioFile == null){
            log.debug("下载合并后文件失败,mergeFilePath:{}",mergeFilePath);
            return RestResponse.validfail(false, "下载合并后文件失败。");
        }

        try (InputStream newFileInputStream = new FileInputStream(minioFile)) {
            //minio上文件的md5值
            String md5Hex = DigestUtils.md5Hex(newFileInputStream);
            //比较md5值，不一致则说明文件不完整
            if(!fileMd5.equals(md5Hex)){
                return RestResponse.validfail(false, "文件合并校验失败，最终上传失败。");
            }
            //文件大小
            uploadFileParamsDto.setFileSize(minioFile.length());
        }catch (Exception e){
            log.debug("校验文件失败,fileMd5:{},异常:{}",fileMd5,e.getMessage(),e);
            return RestResponse.validfail(false, "文件合并校验失败，最终上传失败。");
        }finally {
            if(minioFile!=null){
                minioFile.delete();
            }
        }

        //文件入库
        currentProxy.addMediaFilesToDb(companyId,fileMd5,uploadFileParamsDto,bucket_videoFiles,mergeFilePath);
        //=====清除分块文件=====
        clearChunkFiles(chunkFileFolderPath,chunkTotal);
        return RestResponse.success(true);
    }

    /**
     * 从minio下载文件
     * @param bucket 桶
     * @param objectName 对象名称
     * @return 下载后的文件
     */
    @Override
    public File downloadFileFromMinIO(String bucket,String objectName){
        //临时文件
        File minioFile = null;
        FileOutputStream outputStream = null;
        try{
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            //创建临时文件
            minioFile=File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(stream,outputStream);
            return minioFile;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 得到合并后的文件的地址
     * @param fileMd5 文件id即md5值
     * @param fileExt 文件扩展名
     * @return
     */
    private String getFilePathByMd5(String fileMd5,String fileExt){
        return   fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 +fileExt;
    }

    /**
     * 清除分块文件
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal 分块文件总数
     */
    private void clearChunkFiles(String chunkFileFolderPath,int chunkTotal){

        try {
            List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                    .limit(chunkTotal)
                    .map(i -> new DeleteObject(chunkFileFolderPath.concat(Integer.toString(i))))
                    .collect(Collectors.toList());

            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket("video").objects(deleteObjects).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            results.forEach(r->{
                DeleteError deleteError = null;
                try {
                    deleteError = r.get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("清楚分块文件失败,objectname:{}",deleteError.objectName(),e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清楚分块文件失败,chunkFileFolderPath:{}",chunkFileFolderPath,e);
        }
    }

    //得到分块文件的目录
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "/";
    }



    @Transactional
    public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName){

        //从数据库查询文件
        MediaFiles mediaFiles = new MediaFiles();
        BeanUtils.copyProperties(uploadFileParamsDto,mediaFiles);
        mediaFiles.setId(fileMd5);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setBucket(bucket);
        mediaFiles.setUrl("/" + bucket + "/" + objectName);
        mediaFiles.setFilePath(objectName);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setAuditStatus("002003");
        mediaFiles.setFileSize(uploadFileParamsDto.getFileSize());
        mediaFiles.setFileId(fileMd5);
        mediaFiles.setStatus("1");
        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert < 0) {
            log.error("保存文件信息到数据库失败,{}",mediaFiles.toString());
            XueChengPlusException.cast("保存文件信息失败");
        }
        this.addWaitingTask(mediaFiles);
        log.debug("保存文件信息到数据库成功,{}",mediaFiles.toString());
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles,uploadFileResultDto);
        return uploadFileResultDto;
    }

    /**
     * 添加待处理任务
     * @date 2023-05-25 8:50
     */

    private void addWaitingTask(MediaFiles mediaFiles) {
        String filename = mediaFiles.getFilename();
        int index = filename.lastIndexOf(".");
        String extension = filename.substring(index + 1);
        String mineType = getMimeType(extension);
        if ("video/x-msvideo".equals(mineType)){
            MediaProcess mediaProcess = new MediaProcess();
            BeanUtils.copyProperties(mediaFiles,mediaProcess);
            mediaProcess.setStatus("1");
            int insert = mediaProcessMapper.insert(mediaProcess);
            if (insert < 1)
                throw new XueChengPlusException("添加待处理任务失败");
        }
    }


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

    @Override
    public boolean uploadFileToMinio(String bucketName,String localFilePath,String objectName,String mineType){
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.uploadObject(UploadObjectArgs.builder().
                    bucket(bucket_Files)
                    .filename(localFilePath)
                    .object(objectName)
                    .contentType(mineType)
                    .build()
            );
            log.debug("上传文件到minio成功,bucket:{},objectName:{}",bucketName,objectName);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件到minio出错,bucket:{},objectName:{},错误原因:{}",bucketName,objectName,e.getMessage(),e);
            XueChengPlusException.cast("上传文件到文件系统失败");
            return false;
        }
    }

    @Override
    public MediaFiles getFileById(String mediaId) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(mediaId);
        return mediaFiles;
    }

    public boolean uploadFileToMinio(byte[] bytes, String bucketName,String chunkFilePath){
        try {
            ObjectWriteResponse objectWriteResponse =minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName)
                            .object(chunkFilePath)
                            .stream(new ByteArrayInputStream(bytes),bytes.length,-1)
                            .build());
            log.debug("上传文件到minio成功,bucket:{},objectName:{}",bucketName,chunkFilePath);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件到minio出错,bucket:{},objectName:{},错误原因:{}",bucketName,chunkFilePath,e.getMessage(),e);
            XueChengPlusException.cast("上传文件到文件系统失败");
            return false;
        }
    }

    private String getMimeType(String extension){
        if(extension==null)
            extension = "";
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        //通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    //获取文件的md5
    private String getFileMd5(File file) {
        try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file))) {
            String fileMd5 = DigestUtils.md5Hex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取文件默认存储目录路径 年/月/日
    private String getDefaultFolderPath() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String folder = now.format(dateTimeFormatter);
        return folder;
    }


}
