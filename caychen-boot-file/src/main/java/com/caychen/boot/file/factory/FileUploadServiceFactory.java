package com.caychen.boot.file.factory;

import com.caychen.boot.common.utils.EnumUtils;
import com.caychen.boot.file.abstracts.AbstractFileService;
import com.caychen.boot.file.enums.FileStoreTypeEnum;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @Author: Caychen
 * @Date: 2021/1/3 15:38
 * @Description:
 */
@Configuration(value = FileUploadServiceFactory.FILEUPLOADSERVICEFACTORY_BEAN_NAME)
public class FileUploadServiceFactory {

    public static final String FILEUPLOADSERVICEFACTORY_BEAN_NAME = "fileUploadServiceFactory";

    private static final Map<String, AbstractFileService> map = Maps.newConcurrentMap();

    public FileUploadServiceFactory(@Autowired List<AbstractFileService> fileServiceList) {
        fileServiceList.stream().forEach(fileService ->
                map.put(StringUtils.upperCase(fileService.fileType()), fileService));
    }

    public static AbstractFileService fileService(String fileType) {
        Boolean support = EnumUtils.isSupport(FileStoreTypeEnum.class, fileType);
        if (!support) {
            throw new RuntimeException("不支持的类型：[" + fileType + "]");
        }
        return map.get(StringUtils.upperCase(fileType));
    }
}
