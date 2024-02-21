package com.caychen.boot.oss.service.impl;

import com.aliyun.oss.OSS;
import com.caychen.boot.common.constant.DateConstant;
import com.caychen.boot.common.constant.SymbolConstant;
import com.caychen.boot.common.utils.common.DateUtil;
import com.caychen.boot.common.utils.lang.StringUtils;
import com.caychen.boot.common.utils.random.UUIDUtil;
import com.caychen.boot.oss.config.OssProperties;
import com.caychen.boot.oss.service.IOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * @Author: Caychen
 * @Date: 2021/9/23 15:25
 * @Description:
 */
@Service
@Slf4j
@EnableConfigurationProperties(OssProperties.class)
public class OssServiceImpl implements IOssService {

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private OSS ossClient;

    @Override
    public String fileUpload(String prefix, MultipartFile file) throws IOException {

        prefix = filterInvalidSymbol(prefix);

        //获取文件名称
        String fileName = file.getOriginalFilename();

        String uuid = UUIDUtil.getUUIDWithReplaceBySpace();

        String datePath = DateUtil.formatDate(new Date(), DateConstant.YYYY_MM_DD_SLASH);
        String path = prefix + SymbolConstant.SLASH + datePath;

        if (StringUtils.startsWith(path, SymbolConstant.SLASH)) {
            //防止prefix是个空串
            path = StringUtils.substring(path, 1);
        }

        log.info("文件路径为：[{}]", path);

        String tmpPath = path + SymbolConstant.SLASH + uuid;
        File uploadFile = File.createTempFile(tmpPath, fileName);
        file.transferTo(uploadFile);

        String filepath = path + SymbolConstant.SLASH + uuid + fileName;
        try {
            this.ossClient.putObject(ossProperties.getBucket(), filepath, uploadFile);
            String url = this.getUrl(ossProperties.getBucket(), filepath);
            return url;
        } catch (Exception e) {
            log.error("上传失败：", e);
            throw e;
        } finally {
            if (uploadFile != null) {
                uploadFile.delete();
            }
        }
    }

    private String getUrl(String bucketName, String fileKey) {
        Date expiration = new Date(System.currentTimeMillis() + 3600000L);
        URL url = this.ossClient.generatePresignedUrl(bucketName, fileKey, expiration);
        String urlString = url.toString();

        //切割，把sign去掉
        if (urlString.indexOf(SymbolConstant.QUESTION) != -1) {
            urlString = urlString.split("[?]")[0];
        }

        //http替换成https
        urlString = urlString.replaceFirst("http://", "https://");

        return urlString;
    }

    private String filterInvalidSymbol(String prefix) {
        //去除两边多余的空格
        prefix = StringUtils.trim(prefix);

        //去除两边多余的斜杠
        prefix = prefix
                .replaceAll("^/*", "")
                .replaceAll("/*$", "");

        //自动去除之后，还是以斜杠开头或者结尾，则自动报错
        if (StringUtils.startsWith(prefix, SymbolConstant.SLASH)
                || StringUtils.endsWith(prefix, SymbolConstant.SLASH)) {
            throw new RuntimeException("不能以斜杠开头或者结尾");
        }

        return prefix;
    }
}
