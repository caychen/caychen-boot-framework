package com.caychen.boot.file.support;

import com.caychen.boot.file.IFileService;
import com.caychen.boot.file.abstracts.AbstractFileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: Caychen
 * @Date: 2021/1/3 14:20
 * @Description:
 */
public class DefaultFileServiceSupport implements IFileService {

    private final AbstractFileService delegate;

    public DefaultFileServiceSupport(AbstractFileService delegate) {
        this.delegate = delegate;
    }

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        return this.delegate.uploadFile(path, file);
    }
}
