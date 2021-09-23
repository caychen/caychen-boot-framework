package com.caychen.boot.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author: Caychen
 * @Date: 2021/9/23 15:24
 * @Description:
 */
public interface IOssService {

    String fileUpload(String prefix, MultipartFile file) throws IOException;
}
