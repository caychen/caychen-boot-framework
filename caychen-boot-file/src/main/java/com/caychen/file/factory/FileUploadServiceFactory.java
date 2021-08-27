package com.caychen.file.factory;

import com.caychen.file.abstracts.AbstractFileService;
import com.google.common.collect.Maps;
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
                map.put(fileService.fileType(), fileService));
    }

    public static AbstractFileService fileService(String fileType) {
        return map.get(fileType);
    }
}
