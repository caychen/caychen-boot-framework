package com.caychen.boot.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: Caychen
 * @Date: 2021/1/3 14:39
 * @Description:
 */
public interface IFileService {

    /**
     * 上传文件
     *
     * @param prefix 文件路径前缀，可以用于区分环境（dev、test...）或者各个系统等（PS:该字段不能以斜杠开头）
     * @param file
     * @return
     * @throws IOException
     */
    String uploadFile(String prefix, MultipartFile file) throws IOException;
}
