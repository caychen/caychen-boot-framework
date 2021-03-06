package com.caychen.boot.file.impl;

import com.aliyun.oss.OSS;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.file.abstracts.AbstractFileService;
import com.caychen.boot.file.config.OssProperties;
import com.caychen.boot.file.enums.FileStoreTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/1/3 15:33
 * @Description:
 */
@Service
public class AlibabaFileServiceImpl extends AbstractFileService {

    @Autowired(required = false)
    private OssProperties ossProperties;

    @Autowired(required = false)
    private OSS ossClient;

    @Override
    public String fileType() {
        return FileStoreTypeEnum.OSS.name();
    }

    @Override
    protected String fileUpload(String fileKey, File file) {

        if (ossClient != null) {
            this.ossClient.putObject(ossProperties.getBucket(), fileKey, file);
            return this.getUrl(ossProperties.getBucket(), fileKey);
        } else {
            throw new BusinessException(ErrorEnum.FILE_UPLOAD_ERROR);
        }
    }

    private String getUrl(String bucketName, String fileKey) {
        Date expiration = new Date(System.currentTimeMillis() + 3600000L);
        URL url = this.ossClient.generatePresignedUrl(bucketName, fileKey, expiration);
        String urlString = url.toString();

        return urlString;
    }
}
