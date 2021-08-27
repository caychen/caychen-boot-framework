package com.caychen.file.impl;

import com.caychen.common.enums.ErrorEnum;
import com.caychen.common.exception.BusinessException;
import com.caychen.file.abstracts.AbstractFileService;
import com.caychen.file.config.CosProperties;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/1/3 14:18
 * @Description:
 */
@Service
//@Primary
public class TencentFileServiceImpl extends AbstractFileService {

    @Autowired(required = false)
    private COSClient cosClient;

    @Autowired(required = false)
    private CosProperties cosConfig;

    @Override
    public String fileType() {
        return "COS";
    }

    @Override
    protected String fileUpload(String fileKey, File inputStream) {
        if (cosClient != null) {
            this.cosClient.putObject(cosConfig.getBucket(), fileKey, inputStream);
            return this.getUrl(cosConfig.getBucket(), fileKey);
        } else {
            throw new BusinessException(ErrorEnum.COS_KEY_ERROR);
        }
    }

    private String getUrl(String bucketName, String fileKey) {
        Date expiration = new Date(System.currentTimeMillis() + 3600000L);
        URL url = this.cosClient.generatePresignedUrl(bucketName, fileKey, expiration);
        String urlString = url.toString();

        return urlString;
    }
}
