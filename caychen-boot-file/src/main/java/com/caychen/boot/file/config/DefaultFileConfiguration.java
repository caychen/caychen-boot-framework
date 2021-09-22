package com.caychen.boot.file.config;

import com.caychen.boot.file.factory.FileUploadServiceFactory;
import com.caychen.boot.file.support.DefaultFileServiceSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 17:38
 * @Description:
 */
@Configuration
public class DefaultFileConfiguration {

    @Value("${file.upload.type}")
    private String fileType;

    @Bean
    @DependsOn(value = FileUploadServiceFactory.FILEUPLOADSERVICEFACTORY_BEAN_NAME)
    public DefaultFileServiceSupport defaultFileServiceSupport() {
        if (StringUtils.isBlank(fileType)) {
            throw new RuntimeException("文件服务器不能为空");
        }
        return new DefaultFileServiceSupport(FileUploadServiceFactory.fileService(fileType));
    }
}
